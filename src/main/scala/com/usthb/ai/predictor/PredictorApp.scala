package com.usthb.ai.predictor

import java.io.File

import akka.actor.ActorSystem
import com.typesafe.config.{ConfigFactory, ConfigRenderOptions}
import com.usthb.ai.CmdLineApp
import org.backuity.clist.Command

object PredictorApp extends Command(name = "predictor-app", description = "predictor-app")
  with CmdLineApp {
  override def run(): Unit = {
    val system = ActorSystem("predictor-system", ConfigFactory.load().getConfig("predictor"))
    val predictor = system.actorOf(Predictor.props(), "predictor")
  }
}