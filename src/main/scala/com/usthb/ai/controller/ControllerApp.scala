package com.usthb.ai.controller

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.usthb.ai.main.CmdLineApp
import org.backuity.clist._

object ControllerApp
    extends Command(
      name = "controller-app",
      description =
        "Controller receive gestures and control the mpv player based on the provided " +
          "configuration file example: start or stop the player"
    )
    with CmdLineApp {

  var videoPath = arg[String](description = "video file or playlist folder",
                              required = false,
                              default = s"${sys.env("HOME")}/Videos/")

  override def run(): Unit = {
    val system = ActorSystem("mpv-controller-system", conf.getConfig("player"))
    val player = system.actorOf(MPVPlayer.props(videoPath), "player")
  }
}
