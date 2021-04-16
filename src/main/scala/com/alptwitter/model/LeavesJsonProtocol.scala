package com.alptwitter.model

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

//Some notes on this protocol: 
//Leaves has 23 fields, but this only imports 22 of the fields. 
//Missing the last field

trait LeavesJsonProtocol extends SprayJsonSupport with DefaultJsonProtocol {
  //Below commented line does not work because max jsonFormatX is 22 (need 23...)
  //implicit val leavesFormat = jsonFormat23(Leaves)
  implicit val myUrlFormat = jsonFormat1(MyUrl)
  //implicit val leaves2Format = jsonFormat3(Leaves2)
  implicit val leavesFormat = jsonFormat22(Leaves)



/*
  implicit object LeavesJsonFormat extends RootJsonFormat[Leaves] {
    def write(l: Leaves) = 
      JsArray(
        JsNumber(l.is_archived), 
        JsArray(), 
        JsNumber(l.is_starred), 
        JsString(l.user_name), 
        JsString(l.user_email), 
        JsNumber(l.user_id), 
        JsArray(), 
        JsArray(), 
        JsBoolean(l.is_public), 
        JsString(l.id), 
        JsString(l.title), 
        JsString(l.url), 
        JsString(l.content_text), 
        JsString(l.created_at), 
        JsString(l.updated_at), 
        JsString(l.mimetype), 
        JsString(l.language), 
        JsNumber(l.reading_time), 
        JsString(l.domain_name), 
        JsString(l.preview_picture), 
        JsString(l.http_status), 
        JsArray(), 
        JsString(l.content)
      )

    def read(value: JsValue) = value match {
      case _ => deserializationError("Leaves expected...")
      case JsArray(Vector(JsNumber(is_archived), 
        JsArray(), 
        JsNumber(is_starred), 
        JsString(user_name), 
        JsString(user_email), 
        JsNumber(user_id), 
        JsArray(), 
        JsArray(), 
        JsBoolean(is_public), 
        JsString(id), 
        JsString(title), 
        JsString(url), 
        JsString(content_text), 
        JsString(created_at), 
        JsString(updated_at), 
        JsString(mimetype), 
        JsString(language), 
        JsNumber(reading_time), 
        JsString(domain_name), 
        JsString(preview_picture), 
        JsString(http_status), 
        JsArray(), 
        JsString(content))) => 
        new Leaves(is_archived.toBigDecimal, List("test"), is_starred.toBigDecimal, user_name, user_email, user_id.toBigDecimal, 
        List("test2"), List("test3"), Boolean(is_public), id, title, url, content_text, created_at, updated_at, 
        mimetype, language, reading_time.toBigDecimal, domain_name, preview_picture, http_status, List("test4"), content)
    }
  }
  */
}
