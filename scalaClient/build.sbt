ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.15"

lazy val root = (project in file("."))
  .settings(
    name := "scalaClient"
  )
resolvers += "ksqlDB" at "https://ksqldb-maven.s3.amazonaws.com/maven/"

libraryDependencies += "io.confluent.ksql" % "ksqldb-api-client" % "0.29.0" classifier "with-dependencies"
assembly / assemblyMergeStrategy := {
  case "git.properties" => MergeStrategy.discard
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case PathList("io", "confluent", xs @ _*) => MergeStrategy.first
  case x => (assembly / assemblyMergeStrategy).value(x)
}
