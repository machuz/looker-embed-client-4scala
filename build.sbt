name := "looker-embed-client-4scala"
version := "0.0.1"

scalaVersion in ThisBuild := "2.13.3"

val shttpVer = "1.6.7"
val circeVer = "0.12.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % circeVer,
  "io.circe" %% "circe-generic" % circeVer,
  "io.circe" %% "circe-generic-extras" % circeVer,
  "org.typelevel" %% "cats-core" % "0.9.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2",
  "com.softwaremill.sttp" %% "core" % shttpVer,
  "com.softwaremill.sttp" %% "async-http-client-backend-monix" % shttpVer,
  "com.softwaremill.sttp" %% "circe" % shttpVer
)
