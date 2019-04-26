name := "http4s2"

version := "0.1"

scalaVersion := "2.12.8"

scalacOptions ++= Seq(
  "-Ypartial-unification",
  "-encoding",
  "utf8", // Option and arguments on same line
  "-Xfatal-warnings", // New lines for each options
  "-deprecation",
  "-unchecked",
  "-language:higherKinds"
)

libraryDependencies ++= Seq(
  "org.slf4j" % "slf4j-simple" % "1.7.26",
  "org.http4s" %% "http4s-dsl" % "0.20.0-M7",
  "org.http4s" %% "http4s-blaze-server" % "0.20.0-M7"
)