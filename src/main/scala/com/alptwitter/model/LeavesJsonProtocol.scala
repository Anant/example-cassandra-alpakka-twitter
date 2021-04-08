package com.alptwitter.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

trait LeavesJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  //Below commented line does not work because max jsonFormatX is 22 (need 23...)
  //implicit val leavesFormat = jsonFormat23(Leaves)
  implicit val urlFormat = jsonFormat1(MyUrl)
}
