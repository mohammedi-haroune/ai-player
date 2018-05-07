package com.usthb.ai.collector

import akka.actor.{Actor, DiagnosticActorLogging, Props}
import com.typesafe.config.ConfigFactory
import com.usthb.ai.controller.Action
import com.usthb.ai.predictor._
import javafx.application.Platform

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.util.{Failure, Success, Try}

class CollectorActor(collectorGUI: CollectorGUI)
    extends Actor
    with DiagnosticActorLogging {

  implicit val ex: ExecutionContext =
    scala.concurrent.ExecutionContext.Implicits.global
  private val config = ConfigFactory.load()

  override def receive: Receive = {
    case input: Input =>
      log.debug("receiving input : {}", input)
      //get predictor reference
      val predictorHost =
        config.getString("predictor.akka.remote.netty.tcp.hostname")
      val predictorPort =
        config.getString("predictor.akka.remote.netty.tcp.port")
      val predictorPath =
        s"akka.tcp://predictor-system@$predictorHost:$predictorPort/user/predictor"
      val predictorFuture =
        context.actorSelection(predictorPath).resolveOne(5 second)

      predictorFuture.onComplete {
        case Success(predictor) =>
          predictor ! input
        case Failure(_) =>
          Platform.runLater(() => collectorGUI.predictorNotFound())
      }

    case out: Array[Double] =>
      log.debug("receiving output : {}", out.mkString(","))
      val rounded = out.map(n => if (n < 0.1) 0.01 else n)
      collectorGUI.updateProgressBars(rounded)
      val c = out.zipWithIndex.maxBy(_._1)._2
      log.debug("class = {}", c)
      val gesture = c match {
        case 0 => Fist
        case 1 => Stop
        case 2 => Point1
        case 3 => Point2
        case 4 => Grab
      }
      collectorGUI.setGesture(gesture)

    case gesture: Gesture =>
      log.debug("receiving output : {}", gesture)
      //get player reference
      val playerHost =
        config.getString("player.akka.remote.netty.tcp.hostname")
      val playerPort = config.getString("player.akka.remote.netty.tcp.port")
      val playerPath =
        s"akka.tcp://mpv-controller-system@$playerHost:$playerPort/user/player"
      val playerFuture =
        context.actorSelection(playerPath).resolveOne(5 second)

      playerFuture.onComplete {
        case Success(player) =>
          player ! gesture
        case Failure(_) =>
          Platform.runLater(() => collectorGUI.playerNotFound())
      }

    //none means there is no configuration for the given gesture
    case com.usthb.ai.controller.None =>
      Platform.runLater(() => collectorGUI.noCommandFound())

    case action: Action =>
      Platform.runLater(() => collectorGUI.actionExecuted(action))
  }
}

object CollectorActor {
  def props(collectorGUI: CollectorGUI) =
    Props(new CollectorActor(collectorGUI))
}
