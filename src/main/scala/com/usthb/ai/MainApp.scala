package com.usthb.ai

import com.typesafe.config.ConfigFactory
import com.usthb.ai.collector.CollectorApp
import com.usthb.ai.player.AIPlayerApp
import com.usthb.ai.predictor.PredictorApp
import org.backuity.clist._

trait CmdLineApp { this: Command =>
  var config = opt[String](default = "not stated", description = "configuration file path")
  val conf = config match {
    case "not stated" => ConfigFactory.load()
    case _    => ConfigFactory.load(config)
  }
  def run(): Unit
}

object MainApp {
  def main(args: Array[String]) {
    println(args.mkString(","))
    for {
      app <- Cli
        .parse(args)
        .withCommands(
          CollectorApp,
          AIPlayerApp,
          PredictorApp
        )
    } yield app.run()
  }
}
