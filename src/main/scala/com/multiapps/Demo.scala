package com.multiapps

import org.apache.spark.sql.{SQLContext, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

import scalaz.concurrent.Task

import TaskFactory._

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
      .appName("multiapps")
      .config("spark.ui.port", "8099")
      .config("spark.scheduler.allocation.file", "resources/scheduler.xml")
      .getOrCreate()

    val df = session.read.option("mergeSchema", "true").parquet(inputDir)
    // Create a temp table and cache the dataset
    df.createOrReplaceTempView(s"$mainTableName")

    //session.sqlContext.cacheTable(s"$mainTableName")
    val tasks = for (i <- 0 until numTasks) yield {
      val query = arrayQuery(scala.util.Random.nextInt(length))
      getTask {
        val outputDf = session.sql(s"$query")
        outputDf.collect
      }
    }

    Task.gatherUnordered(tasks).unsafePerformSync
  }
}
