lazy val urlshortener = project.in(file("."))
  .settings(
    name := "urlShortener",
    scalaVersion := "2.13.16",
    libraryDependencies += "com.lihaoyi" %% "cask" % "0.10.2",
    fork := true
  )

lazy val example = project.in(file("example"))
  .settings(
    name := "example",
    scalaVersion := "2.13.16",
    libraryDependencies += "com.lihaoyi" %% "cask" % "0.10.2",
    fork := true
  )
