package com.multiapps

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.io.IO
import akka.routing.FromConfig
import akka.util.Timeout
import com.multiapps.actor.{Driver, RequestSQL, SparkWorker, SprayActor}
import com.typesafe.config.ConfigFactory
import spray.can.Http
import spray.can.server.ServerSettings

import scala.concurrent.Await
import scala.concurrent.duration._

object Demo {
  def main(args: Array[String]): Unit = {

    val mainTableName = "multiappsTable"
    val arrayQuery = Array(s"select distinct (unique_id) from $mainTableName where state_name IN ('Confirm payment')",
      s"select distinct (unique_id) from $mainTableName where user_sex = 'male'",
      s"select distinct (unique_id) from $mainTableName where state_name IN ('Confirm payment') and user_sex = 'male'")
    val length = arrayQuery.length

    var inputDir = "/Users/ydatta/Documents/Workspace/data/new_data/finalest_merge"
    var port = 8089
    if (args.length > 1) {
      port = args(0).toInt
      inputDir = args(1)
    }

    val session = SparkSession.builder()
        .master("local[*]")
      .appName("multiapps")
      .config("spark.ui.port", "8099")
      .config("spark.scheduler.allocation.file", "resources/scheduler.xml")
      .getOrCreate()

    val df = session.read.option("mergeSchema", "true").parquet(inputDir)
    // Create a temp table and cache the dataset
    df.createOrReplaceTempView(s"$mainTableName")

    // Create Actor systems
    val config = ConfigFactory.load()
    implicit val system = ActorSystem("SprayAkkaSpark", config)

    // router is configured in application.conf as a round-robin pool
    val router1: ActorRef = system.actorOf(
      FromConfig.props(Props(classOf[SprayActor], session)), "router1")

    // start a new HTTP server on port 8080 with our service actor as the handler
    IO(Http) ! Http.Bind(router1, interface = "localhost", port = port)

  }
}
