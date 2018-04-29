package com.usthb.ai.collector

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage

import scala.concurrent.duration._
import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.usthb.ai.collector.CollectorGUI

import scala.concurrent.{Await, ExecutionContext};

class CollectorApp extends Application {

  @throws[Exception]
  override def start(stage: Stage): Unit = {
    val config = ConfigFactory.load()
    val host = config.getString("player.akka.remote.netty.tcp.hostname")
    val port = config.getString("player.akka.remote.netty.tcp.port")
    val path = "user/predictor"
    val predictorPath = s"akka.tcp://mpv-controller@$host:$port/$path"


    val system = ActorSystem("collector-system", config.getConfig("collector"))

    val predictorFuture = system.actorSelection(predictorPath).resolveOne(5 seconds)

    println(s"predictorFuture = ${predictorFuture}")

    implicit val ex: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

   val predictor = Await.result(predictorFuture, 5 second)


    val fxmlLoader = new FXMLLoader(getClass.getResource("collector.fxml"))
    val c = new CollectorGUI(predictor)
    fxmlLoader.setController(c)

    val root = fxmlLoader.load[VBox]()

    println(s"predictor = ${predictor}")
    println(s"system = ${system}")
    println(s"fxmlLoader = ${fxmlLoader}")
    println(s"controller = ${c}")

    stage.setTitle("Buyer App")
    val scene = new Scene(root)
    val defaultTheme = "Light"
    val t = getClass.getResource("JMetro" + defaultTheme + "Theme.css").toExternalForm
    scene.getStylesheets.add(t)
    stage.setScene(scene)
    stage.show()
  }
}
