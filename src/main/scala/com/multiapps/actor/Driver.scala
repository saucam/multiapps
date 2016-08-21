package com.multiapps.actor

import akka.actor.{Actor, ActorLogging, ActorRef}

/**
  * Created by ydatta on 8/19/2016 AD.
  */

// Will forward messages to router pool
class Driver(router: ActorRef, numTasks: Int) extends Actor with ActorLogging {

  var counter = 0

  def receive = {
    case msg : Request =>
      log.info("Request: {}", msg)
      router ! msg
    case msg : Response =>
      log.info("Response: {}", msg.text)
      println(s"Response line: ${msg.text}")
      counter += 1
      if (counter == numTasks) {
        context.system.terminate
      }
  }
}
