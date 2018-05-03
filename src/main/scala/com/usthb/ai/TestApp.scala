package com.usthb.ai

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.layout.{AnchorPane, HBox}
import javafx.stage.Stage

class TestApp extends Application {

  @throws[Exception]
  override def start(stage: Stage): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("/fxml/MaterialFxTester.fxml"))
    val root = fxmlLoader.load[AnchorPane]()

    stage.setTitle("Buyer App")
    val scene = new Scene(root)
    val lightTheme = "/css/JMetroLightTheme.css"
    val materialTheme = "/css/material-fx-v0_3.css"
    val t = getClass.getResource(materialTheme).toExternalForm

    scene.getStylesheets.add(t)
    stage.setScene(scene)
    stage.show()
  }
}
