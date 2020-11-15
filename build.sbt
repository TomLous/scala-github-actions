import ReleaseTransformations._

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

//dynverSeparator in ThisBuild := "-"

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

//val relProcessForCommit = Seq[ReleaseStep](
//  inquireVersions,
//  setNextVersion,
//  commitReleaseVersion
//)

def bump(bump: sbtrelease.Version.Bump)(state: State): State = {
  Command.process(
    "release with-defaults",
    Project
      .extract(state)
      .appendWithoutSession(
        Seq(
          releaseProcess := Seq[ReleaseStep](
            inquireVersions,
            setReleaseVersion,
            commitReleaseVersion,
            tagRelease
          ),
          releaseVersionBump := bump
        ),
        state
      )
  )
}

commands += Command.command("bumpPatch")(bump(sbtrelease.Version.Bump.Bugfix))
commands += Command.command("bumpMinor")(bump(sbtrelease.Version.Bump.Minor))
commands += Command.command("bumpMajor")(bump(sbtrelease.Version.Bump.Major))

lazy val showVersion = taskKey[Unit]("Show version")
showVersion := {
  println((version in ThisBuild).value)
}
