package com.usthb.ai.collector

import akka.actor.{Actor, DiagnosticActorLogging, Props}
import com.typesafe.config.ConfigFactory
import com.usthb.ai.predictor._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

class CollectorActor(collectorGUI: CollectorGUI) extends Actor with DiagnosticActorLogging {

  implicit val ex: ExecutionContext =
    scala.concurrent.ExecutionContext.Implicits.global
  private val config = ConfigFactory.load()

  //get predictor reference
  private val predictorHost =
    config.getString("predictor.akka.remote.netty.tcp.hostname")
  private val predictorPort =
    config.getString("predictor.akka.remote.netty.tcp.port")
  private val predictorPath =
    s"akka.tcp://predictor-system@$predictorHost:$predictorPort/user/predictor"
  private val predictorFuture =
    context.actorSelection(predictorPath).resolveOne(5 seconds)
  private val predictor = Await.result(predictorFuture, 5 second)

  //get player reference
  private val playerHost =
    config.getString("player.akka.remote.netty.tcp.hostname")
  private val playerPort = config.getString("player.akka.remote.netty.tcp.port")
  private val playerPath =
    s"akka.tcp://mpv-controller-system@$playerHost:$playerPort/user/player"
  private val playerFuture =
    context.actorSelection(playerPath).resolveOne(5 seconds)
  private val player = Await.result(playerFuture, 5 second)

  log.debug("predictor : {}", predictor)
  log.debug("player : {}", player)

  override def receive: Receive = {
    case input: Input =>
      log.debug("receiving input : {}", input)
      predictor ! input

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
      player ! gesture
  }
}

object CollectorActor {
  def props(collectorGUI: CollectorGUI) = Props(new CollectorActor(collectorGUI))
}
