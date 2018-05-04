package com.usthb.ai.predictor

import akka.actor.{Actor, DiagnosticActorLogging, Props}
import akka.event.LoggingReceive
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.nd4j.linalg.factory.Nd4j

sealed trait Gesture
case object Stop extends Gesture
case object Point1 extends Gesture
case object Point2 extends Gesture
case object Fist extends Gesture
case object Grab extends Gesture

case class Point(x: Double = 0, y: Double = 0, z: Double = 0) {
}

case class Input(p0: Point = Point(),
                 p1: Point = Point(),
                 p2: Point = Point(),
                 p3: Point = Point(),
                 p4: Point = Point(),
                 p5: Point = Point(),
                 p6: Point = Point(),
                 p7: Point = Point(),
                 p8: Point = Point(),
                 p9: Point = Point(),
                 p10: Point = Point(),
                 p11: Point = Point()) {
  def toArray =
    Array(
      p0.x,
      p0.y,
      p0.z,
      p1.x,
      p1.y,
      p1.z,
      p2.x,
      p2.y,
      p2.z,
      p3.x,
      p3.y,
      p3.z,
      p4.x,
      p4.y,
      p4.z,
      p5.x,
      p5.y,
      p5.z,
      p6.x,
      p6.y,
      p6.z,
      p7.x,
      p7.y,
      p7.z,
      p8.x,
      p8.y,
      p8.z,
      p9.x,
      p9.y,
      p9.z,
      p10.x,
      p10.y,
      p10.z,
      p11.x,
      p11.y,
      p11.z
    )
}

class Predictor extends Actor with DiagnosticActorLogging {
  val modelPath = "model.h5"
  val model: MultiLayerNetwork =
    KerasModelImport.importKerasSequentialModelAndWeights(modelPath)

  override def receive: Receive = {
    LoggingReceive {
      case input: Input =>
        val out = predict(input)
        sender() ! out
    }
  }

  def predict(input: Input): Array[Double] = {
    model.output(Nd4j.create(input.toArray)).toDoubleVector
  }
}

object Predictor {
  def props(): Props = Props(new Predictor())
}
