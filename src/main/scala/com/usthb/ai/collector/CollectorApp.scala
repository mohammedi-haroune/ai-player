package com.usthb.ai.collector

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import com.usthb.ai.CmdLineApp
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.layout.HBox
import javafx.scene.{Node, Scene}
import javafx.stage.Stage
import org.backuity.clist._

object CollectorApp
    extends Command(
      name = "collector-app",
      description =
        "Collector collect the input (points coordinates), " +
          "send them to predictor to detect the gesture and then " +
          "to the controller to interact with the player"
    )
    with CmdLineApp {
  override def run(): Unit = {
    Application.launch(classOf[CollectorApp], null)
  }
}

class CollectorApp extends Application {

  @throws[Exception]
  override def start(stage: Stage): Unit = {
    val system =
      ActorSystem("collector-system", CollectorApp.conf.getConfig("collector"))
    val controller = new CollectorGUI()
    val collector =
      system.actorOf(CollectorActor.props(controller), "collector")
    controller.setCollector(collector)
    val fxmlLoader = new FXMLLoader(
      getClass.getResource("/fxml/collector.fxml"))

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
