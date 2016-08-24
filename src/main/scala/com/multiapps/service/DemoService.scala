package com.multiapps.service

import org.apache.spark.sql.SparkSession
import spray.routing.{HttpService, Route}

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future

/**
  * Created by ydatta on 8/24/2016 AD.
  */
trait DemoService  {

  import scala.concurrent.ExecutionContext.Implicits.global

  def session: SparkSession

  def mainService(implicit sql: String): Future[String] = {
    Future {
      val buf = new ArrayBuffer[String]
      val result = session.sql(sql).collect.foreach(x => buf += x.toString)
      buf.toString
    }
  }

}
