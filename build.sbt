name := "ai-player"

version := "0.2"

scalaVersion := "2.12.5"

libraryDependencies ++= Dependencies.dep

maintainer := "MOHAMMEDI Haroune <hm@big-mama.io>"

packageSummary := "AI Player"

packageDescription := """AI Player a media player that can be controlled with hand postures."""

organization := "usthb"

enablePlugins(JavaAppPackaging)

mainClass := Some("com.usthb.ai.main.AIPlayer")

assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat
  case "application.conf"         => MergeStrategy.concat
  case PathList("META-INF", _*)   => MergeStrategy.discard
  case _                          => MergeStrategy.first
}