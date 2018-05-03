package com.usthb.ai

object Main extends App {
  args(0) match {
    case "collector" => com.usthb.ai.collector.CollectorApp.main(args)
    case "predictor" => com.usthb.ai.predictor.PredictorApp.main(args)
    case "player"    => com.usthb.ai.player.AIPlayerApp.main(args)
  }
}
