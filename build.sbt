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

val nextReleaseBump = sbtrelease.Version.Bump.Minor

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

def bumpedVersion(bump: sbtrelease.Version.Bump, state: State)(version: String): String = {
  sbtrelease
    .Version(version)
    .map {
      case v if version == v.withoutQualifier.string =>
        v.bump(bump).withoutQualifier.string
      case v => v.withoutQualifier.string
    }
    .getOrElse(sbtrelease.versionFormatError(version))
}

def nextSnapshotVersion(bump: sbtrelease.Version.Bump, state: State)(version: String): String = {
  val shortHash = vcs(state).currentHash.substring(0, 7)
  sbtrelease
    .Version(version)
    .map(
      _.copy(qualifier = Some(s"-$shortHash-SNAPSHOT")).string
    )
    .getOrElse(sbtrelease.versionFormatError(version))
}

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
          releaseVersion := bumpedVersion(bump, state),
          releaseNextVersion := nextSnapshotVersion(releaseVersionBump.value, state)
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
commands += Command.command("bumpRelease")(
  bump(nextReleaseBump, releaseProcessBumpAndTag)
)
commands += Command.command("bumpSnapshot")(
  bump(nextReleaseBump, releaseProcessSnapshotBump)
)

lazy val showVersion = taskKey[Unit]("Show version")
showVersion := {
  println((version in ThisBuild).value)
}
