lazy val example = project.in(file("example"))
  .settings(
    scalaVersion := "2.13.16",
    libraryDependencies += "com.lihaoyi" %% "cask" % "0.10.2",
    fork := true
  )
