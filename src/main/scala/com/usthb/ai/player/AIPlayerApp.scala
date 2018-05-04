package com.usthb.ai.player

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.usthb.ai.CmdLineApp
import org.backuity.clist._

object AIPlayerApp
    extends Command(name = "player-app", description = "player-app")
    with CmdLineApp {

  var videoPath = arg[String](description =
                                        "video file or playlist folder",
                                      required = false,
                                      default = s"${sys.env("HOME")}/Videos/")

  override def run(): Unit = {
    val system = ActorSystem("mpv-controller-system",
                             ConfigFactory.load().getConfig("player"))
    val player = system.actorOf(MPVPlayer.props(videoPath), "player")
  }
}