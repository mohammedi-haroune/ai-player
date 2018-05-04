name := "ai-player"

version := "0.1"

scalaVersion := "2.12.5"

libraryDependencies ++= Dependencies.dep

lazy val commonSettings = Seq(
  name := "ai-player",
  version := "0.1",
  organization := "com.usthb",
  scalaVersion := "2.12.5",
  libraryDependencies ++= Dependencies.dep
)

lazy val aiPlayer = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    assemblyJarName in assembly := "ai-player.jar",
    mainClass := Some("com.usthb.ai.MainApp"),
    assemblyMergeStrategy in assembly := {
      case PathList("reference.conf") => MergeStrategy.concat
      case "application.conf"         => MergeStrategy.concat
      case PathList("META-INF", _*)   => MergeStrategy.discard
      case _                          => MergeStrategy.first
    }
  )