name := """RememberTheShxt"""
organization := "org.rts"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies += filters
libraryDependencies += jdbc
libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.36"
libraryDependencies += "com.typesafe.play" %% "play-slick" % "2.0.2"
libraryDependencies += "com.typesafe.play" %% "play-mailer" % "5.0.0"

