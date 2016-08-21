package com.multiapps

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

import scalaz.concurrent.Task
import TaskFactory._
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.routing.FromConfig
import com.multiapps.actor.{Driver, RequestSQL, SparkWorker}
import com.typesafe.config.ConfigFactory

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
    var numTasks = 2
    if (args.length > 1) {
      numTasks = args(0).toInt
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
    val system = ActorSystem("AkkaSparkSystem", config)

    // router is configured in application.conf as a round-robin pool
    val router1: ActorRef = system.actorOf(
      FromConfig.props(Props(classOf[SparkWorker], session)), "router1")

    val props = Props(classOf[Driver], router1, numTasks)
    val driver = system.actorOf(props, "driver")

    for (i <- 1 to numTasks) {
      driver ! RequestSQL(arrayQuery(scala.util.Random.nextInt(length)))
    }
    
    Await.result(system.whenTerminated, Duration.Inf)

  }
}
