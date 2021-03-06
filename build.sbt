name := "srt-playground"

organization := "com.github.mtailor"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.0.0"

libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2"

libraryDependencies += "com.github.mtailor" %% "srt-dissector" % "0.1.2"

libraryDependencies += "org.specs2" %% "specs2" % "2.3.12" % Test

libraryDependencies += "io.spray" %% "spray-client" % "1.3.1"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.2"

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.3"

libraryDependencies += "net.lingala.zip4j" % "zip4j" % "1.3.2"


scalacOptions ++= Seq(
  "-feature",
  "-language:implicitConversions"
)
