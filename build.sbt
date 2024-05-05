ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.3"

val lwjglVersion = "3.3.3"
val tinylogVersion = "2.7.0"
val os = "windows"

val lwjglDeps = Seq("lwjgl",
  "lwjgl-glfw",
  "lwjgl-opengl"
).flatMap(module => Seq(
  "org.lwjgl" % module % lwjglVersion,
  "org.lwjgl" % module % lwjglVersion classifier s"natives-$os"
))

val jomlDeps = Seq("org.joml" % "joml" % "1.10.5")

//val tinylogDeps = Seq("tinylog-api-scala", "tinylog-impl").map(module => "org.tinylog" % module % tinylogVersion)

lazy val root = (project in file("."))
  .settings(
    name := "scala3d",
    idePackagePrefix := Some("io.softwarechain.game"),
    libraryDependencies ++= lwjglDeps ++ jomlDeps
  )
