name := "alpakka-twitter"
version := "0.1"
scalaVersion := "2.13.1"
val AkkaVersion = "2.6.13"
val AlpakkaVersion = "2.0.2"

resolvers += Resolver.sonatypeRepo("releases")

//Dependencies mostly coming from https://github.com/akka/akka-quickstart-scala.g8
libraryDependencies ++= Seq(
  "com.danielasfregola" %% "twitter4s" % "7.0",
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.lightbend.akka" %% "akka-stream-alpakka-ftp" % AlpakkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "org.scalatest" %% "scalatest" % "3.1.0" % Test, 
  "ch.qos.logback" % "logback-classic" % "1.2.3"
)

//Dependencies for Alpakka Cassandra
libraryDependencies += "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % "2.0.2"

//Twitter API for Scala: https://github.com/DanielaSfregola/twitter4s
