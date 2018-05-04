package com.usthb.ai.main

import com.typesafe.config.ConfigFactory
import com.usthb.ai.collector.CollectorApp
import com.usthb.ai.controller.PlayerControllerApp
import com.usthb.ai.predictor.PredictorApp
import org.backuity.clist._

trait CmdLineApp { this: Command =>
  var config =
    opt[String](default = "not stated", description = "configuration file path")
  val conf = config match {
    case "not stated" => ConfigFactory.load()
    case _            => ConfigFactory.load(config)
  }
  def run(): Unit
}

object AIPlayer {
  def main(args: Array[String]) {
    println(args.mkString(","))
    for {
      app <- Cli
        .parse(args)
        .withProgramName("ai-player")
        .withDescription(
          "daemon application to run collector, controller, and predictor apps")
        .withCommands(
          CollectorApp,
          PlayerControllerApp,
          PredictorApp
        )
    } yield app.run()
  }
}
