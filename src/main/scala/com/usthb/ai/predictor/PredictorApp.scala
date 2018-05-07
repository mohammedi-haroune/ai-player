package com.usthb.ai.predictor

import akka.actor.ActorSystem
import com.usthb.ai.main.CmdLineApp
import org.backuity.clist._

object PredictorApp
    extends Command(
      name = "predictor-app",
      description =
        "Predictor receive input (points coordinates), pass them to the trained model and send output (gestures predictions)"
    )
    with CmdLineApp {

  var python = arg[String](
    name = "python",
    description =
      "path to python that must have tensorflow, kears, numpy installed")


  var model = opt[String](
    default = "model.h5",
    description = "path to the h5 model file. if not set the default one will be used"
  )

  override def run(): Unit = {
    val system = ActorSystem("predictor-system", conf.getConfig("predictor"))
    val predictor = system.actorOf(PredictorActor.props(python, model), "predictor")
  }
}
