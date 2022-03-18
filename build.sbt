

val scala3Version = "3.1.1"
val weaver = "0.7.10"
val weaverCats = "com.disneystreaming" %% "weaver-cats" % weaver
val weaverDiscipline = "com.disneystreaming" %% "weaver-discipline" % weaver
val weaverScalaCheck = "com.disneystreaming" %% "weaver-scalacheck" % weaver


lazy val root = project
  .in(file("."))
  .settings(
    name := "CliGame",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    // libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % "test",
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.3.5",
      "org.typelevel" %% "cats-core" % "2.7.0",
      "org.typelevel" %% "munit-cats-effect-3" % "1.0.7" % Test,
      weaverCats,
      weaverDiscipline,
      weaverScalaCheck
    )
  )
