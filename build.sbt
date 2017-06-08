name := """eHonigbuch"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc, javaJdbc, "org.postgresql" % "postgresql" % "9.4.1208",
  cache,
  javaWs,
  evolutions,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "com.github.kenglxn.qrgen" % "javase" % "2.2.0",
  "com.google.zxing" % "javase" % "3.2.1"
)

resolvers += "jitpack" at "https://jitpack.io"
