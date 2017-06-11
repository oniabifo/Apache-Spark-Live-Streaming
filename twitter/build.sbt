
lazy val root = (project in file("."))
  .settings(
    name := "apache-spark-ng",
    organization := "com.example",
    scalaVersion := "2.11.8",
    autoScalaLibrary := false,
    version := "0.1.0-SNAPSHOT"
  )

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
   {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
   }
}

libraryDependencies += "org.apache.bahir" % "spark-streaming-twitter_2.11" % "2.1.0"
//libraryDependencies += "org.apache.spark" % "spark-streaming-twitter_2.11" % "1.5.2"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.0.2"
libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.0.2"
libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.1.0"
libraryDependencies += "org.apache.spark" % "spark-tags_2.11" % "2.0.2"
libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.0.2"
libraryDependencies += "org.postgresql" % "postgresql" % "9.3-1102-jdbc41"
libraryDependencies += "org.apache.commons" % "commons-dbcp2" % "2.0.1"
libraryDependencies += "com.datastax.spark" % "spark-cassandra-connector_2.11" % "2.0.0-M3"
//libraryDependencies += "com.datastax.spark" % "spark-cassandra-connector-demos_2.11" % "1.0.6"
libraryDependencies += "com.datastax.spark" % "spark-cassandra-connector-embedded_2.11" % "2.0.1"
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.2.0"
