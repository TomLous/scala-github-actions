// Dynamic versioning: https://github.com/dwijnand/sbt-dynver
addSbtPlugin("com.dwijnand" % "sbt-dynver" % "4.1.1")

// sbt plugin for Scala Code Coverage: https://github.com/scoverage/sbt-scoverage
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")

// sbt plugin for linting: https://github.com/scalastyle/scalastyle-sbt-plugin
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

// Deploy fat JARs. Restart processes: https://github.com/sbt/sbt-assembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.15.0")

// sbt plugin for dockerization: https://github.com/sbt/sbt-native-packager
addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.7.6")

// sbt plugin to build Docker Images: https://github.com/marcuslonnberg/sbt-docker
addSbtPlugin("se.marcuslonnberg" % "sbt-docker" % "1.8.0")
