package com.alptwitter.model

//Case class used to model a row of the Cassandra Table (only id + text of a Tweet)
case class CutTweet(
    id: Long,
    text: String
)
