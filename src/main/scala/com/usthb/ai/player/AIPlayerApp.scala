package com.usthb.ai.player

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object AIPlayerApp extends App {
  val system = ActorSystem("mpv-controller", ConfigFactory.load().getConfig("player"))
  val videoPath = "/home/mohammedi/Videos/ai/"

  val player = system.actorOf(MPVPlayer.props, "player")

  val p0 = Point(54.2638799541,71.4667761379,-64.8077087809)
  val p1 = Point(76.8956347751,42.4624998971,-72.7805451868)
  val p2 = Point(36.6212291601,81.6805569288,-52.9192723727)
  val p3 = Point(85.2322638853,67.7492195029,-73.6841300418)
  val p4 = Point(59.1885757028,10.6789364098,-71.2977813148)

  val input = Input(p0, p1, p2, p3, p4)

  val predictor = system.actorOf(Predictor.props(player), "predictor")
}
