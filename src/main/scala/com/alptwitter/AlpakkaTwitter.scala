package com.alptwitter

import akka.actor.typed.ActorRef
import akka.actor.ActorSystem
import akka.actor.typed.Behavior
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.scaladsl.{Sink, Source}
import akka.stream.ActorMaterializer
import akka.stream.alpakka.cassandra.CassandraSessionSettings
import akka.stream.alpakka.cassandra.scaladsl.CassandraSession
import akka.stream.alpakka.cassandra.scaladsl.CassandraSessionRegistry
import akka.stream.alpakka.cassandra.CassandraWriteSettings
import akka.stream.alpakka.cassandra.scaladsl.CassandraFlow
import akka.stream.alpakka.cassandra.scaladsl.CassandraSource
import com.datastax.oss.driver.api.core.cql.{BoundStatement, PreparedStatement}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Success, Failure, Random}

import com.danielasfregola.twitter4s.TwitterStreamingClient
import com.danielasfregola.twitter4s.entities.Tweet

//Prints errors about "Failed to connect with protocol DSE_V2 or DSE_V1" are not actually errors/warnings
//See https://community.datastax.com/questions/6847/spring-data-cassandra-connectivity-issue.html
//Datastax java driver is now unified for both OSS and DSE Cassandra, and DSE_V2 and DSE_V1 are for Cassandra.

//Case class used to model a row of the Cassandra Table (only id + text of a Tweet)
case class CutTweet(id: Long, text: String)

object AlpakkaTwitter extends App {

  val keyspace = "testkeyspace"
  val table = "testtable"
  val randomizer = new Random()

  //Akka ActorSystem and ActorMaterializer,  implicitly used by other things
  implicit val system: ActorSystem = ActorSystem()
  implicit lazy val materializer: ActorMaterializer = ActorMaterializer()
  val sessionSettings = CassandraSessionSettings()
  implicit val cassandraSession: CassandraSession =
    CassandraSessionRegistry.get(system).sessionFor(sessionSettings)

  //This is for pulling Tweets from Twitter
  val streamingClient = TwitterStreamingClient()
  val trackedWords = Seq("#csgo")
  val statementBinder: (CutTweet, PreparedStatement) => BoundStatement =
    (insert, preparedStatement) =>
      preparedStatement.bind(insert.id, insert.text)

  streamingClient.filterStatuses(tracks = trackedWords) {
    case tweet: Tweet => {
      tweet.retweeted_status match {
        case None => {
          println("Found a non-retweeted tweet!")
          //Now we want to save the Tweet using Alpakka Cassandra
          val testInsert = Seq(CutTweet(tweet.id, tweet.text))
          val written: Future[Seq[CutTweet]] = Source(testInsert)
            .via(
              CassandraFlow.create(
                CassandraWriteSettings.defaults,
                s"INSERT INTO $keyspace.$table(id, excerpt) VALUES (?, ?)",
                statementBinder
              )
            )
            .runWith(Sink.seq)

          written.onComplete({
            case Success(value) => {
              //Successfully wrote tweet to Cassandra
            }
            case Failure(exception) => {
              exception.printStackTrace
            }
          })
        }
        case Some(tweet2) => {
          println("This is a retweet of a previous tweet, will not save")
        }
      }
    }
  }

  val version: Future[String] =
    cassandraSession
      .select("SELECT release_version FROM system.local;")
      .map(_.getString("release_version"))
      .runWith(Sink.head)

  //Prints version number of Cassandra being used
  version.onComplete({
    case Success(value) => {
      println()
      println(value)
    }
    case Failure(exception) => {
      exception.printStackTrace
    }
  })

  //Example of how to pull from Cassandra, pull from custom table
  /*
  val tableV: Future[String] =
  CassandraSource(s"SELECT * FROM $keyspace.$table where id=37")
    .map(_.getString("excerpt"))
    .runWith(Sink.head)
   */

}
