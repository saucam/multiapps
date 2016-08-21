package com.multiapps.actor

import akka.actor.{Actor, ActorLogging}
import org.apache.spark.sql.SparkSession

import scala.collection.mutable.ArrayBuffer

/**
  * Created by ydatta on 8/19/2016 AD.
  */

class Request
case class RequestSQL(text: String) extends Request
case class RequestML() extends Request
case class RequestMapRed() extends Request
case class Response(text: String)


class SparkWorker(session: SparkSession) extends Actor with ActorLogging {

  def receive = {
    case RequestSQL(sql) =>
      log.info("sql: {}", sql)
      val buf = new ArrayBuffer[String]
      val result = session.sql(sql).collect.foreach(x => buf += x.toString)
      sender() ! Response(buf.toString)
  }

}
