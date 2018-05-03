package com.usthb.ai.collector

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.layout.HBox
import javafx.scene.{Node, Scene}
import javafx.stage.Stage

object CollectorApp extends App {
  Application.launch(classOf[CollectorApp], args: _*)
}

class CollectorApp extends Application {

  @throws[Exception]
  override def start(stage: Stage): Unit = {
    val system = ActorSystem("collector-system", ConfigFactory.load().getConfig("collector"))
    val controller = new CollectorGUI()
    val collector = system.actorOf(CollectorActor.props(controller), "collector")
    controller.setCollector(collector)
    val fxmlLoader = new FXMLLoader(getClass.getResource("/fxml/collector.fxml"))
    fxmlLoader.setController(controller)
    val root = fxmlLoader.load[HBox]()

    println(s"collector = ${collector}")
    println(s"system = ${system}")
    println(s"fxmlLoader = ${fxmlLoader}")
    println(s"controller = ${controller}")

    stage.setTitle("Collector App")
    val scene = new Scene(root)
    val lightTheme = "/css/JMetroLightTheme.css"
    val materialTheme = "/css/material-fx-v0_3.css"
    val t = getClass.getResource(materialTheme).toExternalForm
    scene.getStylesheets.add(t)
    stage.setScene(scene)
    stage.show()
  }
}
