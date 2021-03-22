# Alpakka Cassandra + Twitter4Scala

## About this Project
This project uses Alpakka Cassandra 2.0 and Scala to pull new Tweets from Twitter for a given hashtag (or set of hashtags) using Twitter API v1.1 (through twitter4s scala twitter client) and write them into a local Cassandra database. 

The project will only save tweets which are not a retweet of another tweet. Currently only saves the truncated version (<=140 chars) of tweets. 

- - - 

## Requirements
```
Scala 2.12+
JDK 8
sbt 1.3.1+ (project uses 1.4.8, unsure about this line)
Docker (and required RAM for running a Cassandra container)
```
- - -

## Table of Contents
1. [Setup and run local Cassandra using Docker](#Cassandra-Setup)
2. [Configure Twitter API keys + hashtag to filter by](#Twitter-Setup)
3. [Setup hashtags and run the project using SBT](#Running-The-Project)
4. [Observe results in Cassandra using cqlsh](#Observe-Tables)

- - - 
## Cassandra Setup
Make sure you have docker installed on your machine. Run the following docker command to pull up a local Cassandra container with port 9042 exposed: 

```
docker run -p 9042:9042 --rm --name my-cassandra -d cassandra
```
Make sure your container is running (may need to give the container a few minutes to boot up): 
```
docker ps -a
```
[Screenshot goes here?]  
Afterwards, run CQLSH on the container in interactive terminal mode to setup keyspace and tables: 
```
docker exec -it my-cassandra cqlsh
```

Once CQLSH comes up, create the necessary keyspace and table for this demo.\
(mention adding test record? INSERT INTO testkeyspace.testtable(id, excerpt) VALUES (37, "appletest")): 
```
CREATE KEYSPACE testkeyspace WITH replication = {'class': 'SimpleStrategy', 'replication_factor': '1'}  AND durable_writes = true;

CREATE table testkeyspace.testtable(id bigint PRIMARYKEY, excerpt text);  

exit
```

## Twitter Setup
From the root folder of this repository, browse to the application.conf.example file found in /src/main/resources/application.conf.example. Copy this file into this same directory and rename it application.conf  
Go to the [twitter developer dashboard](https://developer.twitter.com/en/portal/dashboard) website, register an application and insert these four twitter api keys into this portion of application.conf: 
```
twitter {
  consumer {
    key = "consumer-key-here"
    secret = "consumer-secret-here"
  }
  access {
    key = "access-key-here"
    secret = "access-token-here"
  }
}
```
- - -

## Running The Project
Navigate to /src/main/scala/com/alptwitter/AlpakkaTwitter.scala and change the following line to indicate what hashtags you wish to look at new tweets for: 
```
val trackedWords = Seq("#myHashtag")
```
If you want to track more than one hashtag, add more by adding more strings and separating with commas. 

The project can then be run by navigating to the root folder of the project and running: 
```
sbt run
```
As new tweets are posted which contain any of the hashtags listed in the trackedWords variable, a message will print in the console which says whether the tweet was a retweet or a unique tweet.
- - -
## Observe Tables
As new tweets (not retweets of tweets) with your entered hashtags are posted and found, they will be saved to Cassandra as a (tweet id, text of tweet) entry in testkeyspace.testtable. To check that the tweets are being saved to Cassandra, run CQLSH on the cassandra container and observe the table: 

```
docker exec -it my-cassandra cqlsh
SELECT * FROM testkeyspace.testtable; 
```

- - -
## References / Useful Links: 
### [Twitter4S (Twitter for Scala) Github Repository](https://github.com/DanielaSfregola/twitter4s)
### [Twitter4S definition of Tweet object](https://github.com/DanielaSfregola/twitter4s/blob/master/src/main/scala/com/danielasfregola/twitter4s/entities/Tweet.scala)
### [Alpakka Cassandra Documentation](https://doc.akka.io/docs/alpakka/2.0.2/cassandra.html)