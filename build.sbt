name := "srt-playground"

organization := "com.github.mtailor"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "com.github.mtailor" %% "srt-dissector" % "0.1"

libraryDependencies += "org.specs2" %% "specs2" % "2.3.12" % Test

libraryDependencies += "io.spray" %% "spray-client" % "1.3.1"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.3.2"

libraryDependencies += "org.jsoup" % "jsoup" % "1.7.3"


scalacOptions ++= Seq(
  "-feature",
  "-language:implicitConversions"
)
