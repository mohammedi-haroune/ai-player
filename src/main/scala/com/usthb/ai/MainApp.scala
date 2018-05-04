package com.usthb.ai

import com.usthb.ai.collector.CollectorApp
import com.usthb.ai.player.AIPlayerApp
import com.usthb.ai.predictor.PredictorApp
import org.backuity.clist._

trait CmdLineApp { this: Command =>
  var verbose: Boolean = opt[Boolean](abbrev = "v")
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
