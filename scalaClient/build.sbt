ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

lazy val root = (project in file("."))
  .settings(
    name := "scalaClient"
  )
libraryDependencies += "com.lihaoyi" %% "requests" % "0.8.0"

libraryDependencies += "com.typesafe.play" %% "play" % "2.8.21"

assembly / assemblyMergeStrategy := {
  case "git.properties" => MergeStrategy.discard
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("io", "confluent", xs @ _*) => MergeStrategy.first
  case PathList("module-info.class") => MergeStrategy.discard
  case x => (assembly / assemblyMergeStrategy).value(x)
}

