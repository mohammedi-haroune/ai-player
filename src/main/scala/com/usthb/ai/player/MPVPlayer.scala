package com.usthb.ai.player

import java.io.{BufferedWriter, File}
import java.nio.file.{Files, Paths}

import akka.actor.{FSM, LoggingFSM, Props}
import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.sys.process._

sealed trait State
sealed trait Data
sealed trait Action

case object Aborted extends State
case object Paused extends State
case object Stopped extends State
case object Playing extends State

case object Stop extends Action
case object Start extends Action
case object Pause extends Action
case object Play extends Action
case object Abort extends Action
case object Seek extends Action

case object Empty extends Data

class MPVPlayer extends LoggingFSM[State, Data] {
  import MPVPlayer._
  var process: Process = _

  private val config = ConfigFactory.load().getConfig("commands")

  log.debug("config = {}", config.toString)

  startWith(Aborted, Empty)

  setTimer("Refresher", Refresh, 10 second, true)

  when(Aborted)(FSM.NullFunction)
  when(Playing)(FSM.NullFunction)
  when(Paused)(FSM.NullFunction)
  when(Stopped)(FSM.NullFunction)

  whenUnhandled {
    case Event(Refresh, _) =>
      log.info("refreshing states when {}", stateName)
      if (process == null) {
        log.info("MPV Player is aborted")
        goto(Aborted)
      } else if (process.isAlive()) {
        if (isPaused) {
          log.info("MPV Player is paused")
          goto(Paused)
        } else stay()
      } else {
        log.info("MPV Player is aborted")
        goto(Aborted)
      }

    case Event(gesture: Gesture, _) =>
      val state = stateName.getClass.getSimpleName.filter(_ != '$').toLowerCase
      val gestureName = gesture.getClass.getSimpleName.filter(_ != '$').toLowerCase
      log.debug("state = {}", state)
      val cmd = s"${config.getString(s"$state.$gestureName")}"

      log.debug("running cmd: {}", cmd)

      cmd match {
        case "start" =>
          process = launch(videoPath)
          goto(Playing)

        case "exit" =>
          exit(process)
          goto(Aborted)

        case "pause" =>
          val msg = sendMessage(config.getString(s"actions.$cmd"))
          log.info("sent msg {}", msg)
          goto(Paused)

        case "play" =>
          val msg = sendMessage(config.getString(s"actions.$cmd"))
          log.info("sent msg {}", msg)
          goto(Playing)

        case "resume" =>
          val msg = sendMessage(config.getString(s"actions.$cmd"))
          log.info("sent msg {}", msg)
          goto(Playing)

        case "seek10" =>
          if (stateName == Playing) {
            val msg = sendMessage(config.getString(s"actions.$cmd"))
            log.info("sent msg {}", msg)
            stay()
          } else throw new Exception("can't seek when not playing")

        case _ =>
          log.debug(s"$cmd not yet supported staying in the actual state $state")
          stay()
      }
  }
}

object MPVPlayer {
  def props: Props = Props(new MPVPlayer())
  val commandsPath = "/tmp/mpvsend"
  val ipcPath = "/tmp/mpvsocket"
  val videoPath = "/home/mohammedi/Videos/ai/"

  def launch(path: String): Process = {
    val runPlayer =
      Seq("mpv", path, "--no-terminal", "--input-ipc-server", ipcPath)
    runPlayer.run
  }

  def exit(process: Process): Unit = {
    process.destroy()
  }

  def sendMessage(msg: String): String = {
    val writer: BufferedWriter = Files.newBufferedWriter(Paths.get(commandsPath))
    //val file = new File(commandsPath)
    writer.write(msg + "\n")
    writer.close()

    val r = Seq("socat", commandsPath, ipcPath).!!
    //file.delete()
    println(s"send message $msg with error code $r")
    r
  }

  def isPaused: Boolean = {
    val msg = """{ "command": ["get_property", "pause"] }"""
    val writer = Files.newBufferedWriter(Paths.get(commandsPath))
    writer.write(msg + "\n")
    writer.close()
    Seq("socat", commandsPath, ipcPath) !
    val reader = Files.readAllLines(Paths.get(commandsPath)).asScala
    new File(commandsPath).delete()
    reader.filter(_ != msg).head.contains("true")
  }
}

object Refresh
