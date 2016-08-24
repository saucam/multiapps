package com.multiapps.actor

import akka.actor.{Actor, ActorLogging, ActorRef}
import com.multiapps.routes.SQLRoute
import com.multiapps.service.DemoService
import org.apache.spark.sql.SparkSession

/**
  * Created by ydatta on 8/24/2016 AD.
  */
class SprayActor(sqlSession: SparkSession) extends Actor with DemoService with SQLRoute with ActorLogging {

  def session: SparkSession = sqlSession
  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  def receive = runRoute (
    routes
  )
}
