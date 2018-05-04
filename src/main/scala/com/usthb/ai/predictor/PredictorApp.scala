package com.usthb.ai.predictor

import java.io.File

import akka.actor.ActorSystem
import com.typesafe.config.{ConfigFactory, ConfigRenderOptions}
import com.usthb.ai.main.CmdLineApp
import org.backuity.clist.{Command, opt}

object PredictorApp
    extends Command(name = "predictor-app",
                    description =
                      "Predictor receive input (points coordinates), pass them to the trained model and send output (gestures predictions)")
    with CmdLineApp {
  var python = opt[String](default = "not stated", description = "path to python that must have tensorflow, kears, numpy installed")

  override def run(): Unit = {
    val system = ActorSystem("predictor-system", conf.getConfig("predictor"))
    val predictor = system.actorOf(Predictor.props(python), "predictor")
  }
}
