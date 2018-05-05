package com.usthb.ai.controller

import java.io.{BufferedWriter, File}
import java.nio.file.{Files, Paths}

import akka.actor.{FSM, LoggingFSM, Props}
import com.typesafe.config.{Config, ConfigFactory}
import com.usthb.ai.predictor.Gesture

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.reflect.runtime.universe
import scala.sys.process._

sealed trait State
case object Aborted extends State
case object Paused extends State
case object Playing extends State

sealed trait Action
sealed trait StaticAction extends Action
case object Start extends Action
case object Pause extends Action
case object Resume extends Action
case object Play extends Action
case object Abort extends Action
case object Seek extends StaticAction
case object RevertSeek extends StaticAction
case object None extends StaticAction
case object PlayListNext extends StaticAction
case object PlayListPrev extends StaticAction
case object ShowProgress extends StaticAction
case object VolumeUp extends StaticAction
case object VolumeDown extends StaticAction
case object SpeedUp extends StaticAction
case object SpeedDown extends StaticAction

sealed trait Data
case object Empty extends Data
class MPVPlayer(videoPath: String, commandsPath: String) extends LoggingFSM[State, Data] {

  import MPVPlayer._

  var process: Process = _
  startWith(Aborted, Empty)
  setTimer("Refresher", Refresh, 10 second, repeat = true)
  when(Aborted) {
    case Event(Start, _) =>
      process = launch(videoPath)
      goto(Playing)
  }
  when(Playing) {
    case Event(Pause, _) =>
      sendMessage(actions(Pause))
      goto(Paused)

    case Event(a: StaticAction, _) =>
      sendMessage(actions(a))
      stay()
  }
  when(Paused) {
    case Event(Resume, _) =>
      sendMessage(actions(Resume))
      goto(Playing)
    case Event(a: StaticAction, _) =>
      sendMessage(actions(a))
      stay()
  }
  whenUnhandled {
    case Event(gesture: Gesture, _) =>
      val config = ConfigFactory.parseFile(new File(commandsPath))

      log.debug("config", config.origin().filename())

      val state = stateName.getClass.getSimpleName.filter(_ != '$').toLowerCase
      val gestureName =
        gesture.getClass.getSimpleName.filter(_ != '$').toLowerCase
      log.debug("state = {}", state)
      val cmdStringName = config.getString(s"$state.$gestureName")
      log.debug("running cmd: {}", cmdStringName)
      val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)
      val module = runtimeMirror.staticModule(cmdStringName)
      val cmdObject = runtimeMirror.reflectModule(module).instance
      log.debug("reflected type: {}", cmdObject)
      log.debug("state : {}", stateName)
      self ! cmdObject.asInstanceOf[Action]
      stay()

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
    case Event(Abort, _) =>
      exit(process)
      goto(Aborted)
    case Event(action, _) =>
      log.debug(
        s"$action not yet supported staying in the actual state $stateName")
      stay()
  }
}
object MPVPlayer {
  def props(videoPath: String, commandsPath: String): Props = Props(new MPVPlayer(videoPath, commandsPath))

  val commandsPath = "/tmp/mpvsend"
  val ipcPath = "/tmp/mpvsocket"

  val actions = Map[Action, String](
    Play -> """{ "command": ["set_property", "pause", true] }""",
    Pause -> """{ "command": ["set_property", "pause", true] }""",
    Resume -> """{ "command": ["set_property", "pause", false] }""",
    Seek -> """{ "command": ["seek", 10] }""",
    RevertSeek -> """{ "command": ["revert-seek", 10] }""",
    PlayListNext -> """{ "command": ["playlist-next"] }""",
    PlayListPrev -> """{ "command": ["playlist-prev"] }""",
    ShowProgress -> """{ "command": ["show-progress"] }""",
    VolumeUp -> """{ "command": ["multiply", "volume", "2"] }""",
    VolumeDown -> """{ "command": ["multiply", "volume", "0.5"] }""",
    SpeedUp -> """{ "command": ["multiply", "speed", "2"] }""",
    SpeedDown -> """{ "command": ["multiply", "speed", "0.5"] }"""
  )

  def launch(path: String): Process = {
    val runPlayer =
      Seq("mpv", path, "--no-terminal", "--input-ipc-server", ipcPath)
    runPlayer.run
  }

  def exit(process: Process): Unit = {
    process.destroy()
  }

  def sendMessage(msg: String): String = {
    val writer: BufferedWriter =
      Files.newBufferedWriter(Paths.get(commandsPath))
    writer.write(msg + "\n")
    writer.close()
    Seq("socat", commandsPath, ipcPath).!!
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
