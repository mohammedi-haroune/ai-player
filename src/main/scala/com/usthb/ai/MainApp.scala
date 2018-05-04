package com.usthb.ai

import org.backuity.clist._

trait Application { this: Command =>
  var verbose = opt[Boolean](abbrev = "v")
  def run(): Unit
}

object SilverWatch {
  def main(args: Array[String]) {
    for {
      app <- Cli
        .parse(args)
        .withCommands()
    } yield app.run()
  }
}
