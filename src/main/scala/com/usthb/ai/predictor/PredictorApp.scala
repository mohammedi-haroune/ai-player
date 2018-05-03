package com.usthb.ai.predictor

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object PredictorApp extends App {
  val system = ActorSystem("predictor-system", ConfigFactory.load().getConfig("predictor"))
  val predictor = system.actorOf(Predictor.props(), "predictor")
}
