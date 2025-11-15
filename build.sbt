lazy val urlshortener = project.in(file("."))
  .settings(
    scalaVersion := "2.13.16",
    libraryDependencies += "com.lihaoyi" %% "cask" % "0.10.2",
    fork := true
  )
