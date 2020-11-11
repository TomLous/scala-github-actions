name := "scala-github-actions"

scalaVersion := "2.12.10"

organization := "info.graphiq"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.2" % Test
)

Test / testOptions += Tests.Argument("-oDT")
Test / parallelExecution := false

assembly / assemblyOption := (assemblyOption in assembly).value.copy(includeScala = false)
assembly / assemblyOutputPath := baseDirectory.value / "output" / "assembly.jar"
assembly / logLevel := sbt.util.Level.Error
assembly / test := {}
pomIncludeRepository := { _ =>
  false
}

dynverSeparator in ThisBuild := "-"

scalacOptions ++= Seq(
  "-deprecation",
  "-unchecked",
  "-Xlint",
  "-Xlint:missing-interpolator",
  "-Ywarn-macros:after",
  "-Ywarn-unused",
  "-Ywarn-dead-code",
  "-language:_",
  "-encoding",
  "UTF-8"
)

lazy val showVersion = taskKey[Unit]("Show version")
showVersion := {
  println((version in ThisBuild).value)
}
