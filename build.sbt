import ReleaseTransformations._
import sbtrelease.Utilities._

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

val releaseProcessBumpAndTag: Seq[ReleaseStep] = Seq(
  inquireVersions,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease
)

val releaseProcessSnapshotBump: Seq[ReleaseStep] = Seq(
  inquireVersions,
  setNextVersion,
  commitNextVersion
)

def bump(bump: sbtrelease.Version.Bump, steps: Seq[ReleaseStep])(
    state: State
): State = {
  Command.process(
    "release with-defaults",
    Project
      .extract(state)
      .appendWithoutSession(
        Seq(
          releaseVersionBump := bump,
          releaseProcess := steps,
          releaseNextVersion := { ver =>
            sbtrelease
              .Version(ver)
              .map(
                _.bump(releaseVersionBump.value)
                  .copy(qualifier = Some("fff"))
                  .string
              )
              .map(x => { println(x); x })
              .getOrElse(sbtrelease.versionFormatError(ver))
          }
        ),
        state
      )
  )
}

def vcs(state: State): sbtrelease.Vcs =
  Project
    .extract(state)
    .get(releaseVcs)
    .getOrElse(
      sys.error("Aborting release. Working directory is not a repository of a recognized VCS.")
    )

commands += Command.command("bumpPatch")(
  bump(sbtrelease.Version.Bump.Bugfix, releaseProcessBumpAndTag)
)
commands += Command.command("bumpMinor")(
  bump(sbtrelease.Version.Bump.Minor, releaseProcessBumpAndTag)
)
commands += Command.command("bumpMajor")(
  bump(sbtrelease.Version.Bump.Major, releaseProcessBumpAndTag)
)
commands += Command.command("bumpSnapshot")(
  bump(sbtrelease.Version.Bump.Minor, releaseProcessSnapshotBump)
)

lazy val showVersion = taskKey[Unit]("Show version")
showVersion := {
  println((version in ThisBuild).value)
}
