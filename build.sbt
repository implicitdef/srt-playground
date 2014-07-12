name := "srt-playground"

organization := "com.github.mtailor"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.11.1"

libraryDependencies += "commons-io" % "commons-io" % "2.4"

libraryDependencies += "com.github.mtailor" %% "srt-dissector" % "0.1"

libraryDependencies += "org.specs2" %% "specs2" % "2.3.12" % Test

scalacOptions ++= Seq(
  "-feature",
  "-language:implicitConversions"
)
