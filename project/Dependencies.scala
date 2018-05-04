import sbt.Keys.scalaVersion
import sbt._

object Version {
  val akka = "2.5.6"
  val akkaHttp = "10.0.10"
  val scalaTest = "3.0.4"
  val protobuf = "3.4.0"
  val slf4j = "1.7.25"
  val cats = "1.0.0-RC1"
  val msgPack = "0.6.11"
  val argonaut = "6.2"
  val akkaStreamKafka = "0.17"
  val typesafe = "1.3.2"
  val levelDB = "0.9"
  val levelDBJINI = "1.8"
  val twilio = "7.15.4"
  val grizzled = "1.3.1"
  val akkaElastic = "1.1.0"
  val elasticClient = "5.5.3"
  val sprayJSON = "1.3.4"
  val slick = "3.2.1"
  val alpakkaSlick = "0.14"
  val postgresql = "42.1.4"
  val clist = "3.4.0"
  val elasticTest = "6.1.2.0"
  val firebase = "5.8.0"
  val zk = "0.10"
  var embededKafka = "1.1.0"
  val nd4j = "1.0.0-alpha"
  val dl4j = "1.0.0-alpha"
  val scala = "2.12.5"
}

object Library {
  val typesafe = "com.typesafe" % "config" % Version.typesafe
  val akka = "com.typesafe.akka" %% "akka-actor" % Version.akka
  val slick = "com.typesafe.slick" % "slick_2.12" % Version.slick
  val hikaricpSlick = "com.typesafe.slick" %% "slick-hikaricp" % Version.slick
  val alpakkaSlick = "com.lightbend.akka" %% "akka-stream-alpakka-slick" % Version.alpakkaSlick
  val postgressql = "org.postgresql" % "postgresql" % Version.postgresql
  val akkaPersistence = "com.typesafe.akka" %% "akka-persistence" % Version.akka
  val akkaCluster = "com.typesafe.akka" %% "akka-cluster" % Version.akka
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % Version.akkaHttp
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % Version.akka
  val akkaStreamKafka = "com.typesafe.akka" %% "akka-stream-kafka" % Version.akkaStreamKafka
  val protobuf = "com.google.protobuf" % "protobuf-java" % Version.protobuf
  val akkaContrib = "com.typesafe.akka" %% "akka-contrib" % Version.akka
  val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % Version.akka
  val akkaStreamTestKit = "com.typesafe.akka" %% "akka-stream-testkit" % Version.akka
  val akkaRemote = "com.typesafe.akka" %% "akka-remote" % Version.akka
  val scalaTest = "org.scalatest" %% "scalatest" % Version.scalaTest
  val slf4j = "org.slf4j" % "slf4j-simple" % Version.slf4j
  val slf4jApi = "org.slf4j" % "slf4j-api" % Version.slf4j
  val cats = "org.typelevel" %% "cats-core" % Version.cats
  val scalaReflection = "org.scala-lang" % "scala-reflect" % Version.scala
  val msgPack = "org.msgpack" %% "msgpack-scala" % Version.msgPack
  val argonaut = "io.argonaut" %% "argonaut" % Version.argonaut
  val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % Version.akka
  val grizzled = "org.clapper" %% "grizzled-slf4j" % Version.grizzled
  val levelDB = "org.iq80.leveldb" % "leveldb" % Version.levelDB
  val levelDBJINI = "org.fusesource.leveldbjni" % "leveldbjni-all" % Version.levelDBJINI
  val twilio = "com.twilio.sdk" % "twilio" % Version.twilio
  val akkaElastic = "com.github.takezoe" %% "akka-stream-elasticsearch" % Version.akkaElastic
  val elasticClient = "org.elasticsearch.client" % "rest" % Version.elasticClient
  val sprayJSON = "io.spray" %% "spray-json" % Version.sprayJSON
  val clistCore = "org.backuity.clist" %% "clist-core" % Version.clist
  val clistMacros = "org.backuity.clist" %% "clist-macros" % Version.clist % "provided"
  val catsLaw = "org.typelevel" %% "cats-laws" % Version.cats
  val catsTestKit = "org.typelevel" %% "cats-testkit" % Version.cats
  val shpelessTest = "com.github.alexarchambault" %% "scalacheck-shapeless_1.13" % "1.1.6"
  // https://mvnrepository.com/artifact/org.codelibs/elasticsearch-cluster-runner
  val elasticTest = "org.codelibs" % "elasticsearch-cluster-runner" % Version.elasticTest
  val firebase = "com.google.firebase" % "firebase-admin" % Version.firebase
  val zk = "com.101tec" % "zkclient" % Version.zk
  val embeddedKafka = "net.manub" %% "scalatest-embedded-kafka" % Version.embededKafka
  val dl4jModelImport = "org.deeplearning4j" % "deeplearning4j-modelimport" % Version.dl4j
  val nd4jNative = "org.nd4j" % "nd4j-native-platform" % Version.nd4j
}

object Dependencies {

  import Library._

  val dep = Seq(
    akka,
    akkaRemote,
    slf4j,
    slf4jApi,
    typesafe,
    scalaReflection,
    clistCore,
    clistMacros
  )
}