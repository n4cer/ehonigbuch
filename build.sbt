name := """eHonigbuch"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.13.10"

libraryDependencies ++= Seq(
  jdbc, javaJdbc, "org.postgresql" % "postgresql" % "42.5.1",
  guice,
  ehcache,
  javaWs,
  evolutions,
  "org.mindrot" % "jbcrypt" % "0.4",
  "com.github.kenglxn.QRGen" % "javase" % "2.7.0",
  "com.google.zxing" % "javase" % "3.2.1"
)

resolvers += "jitpack" at "https://jitpack.io"
