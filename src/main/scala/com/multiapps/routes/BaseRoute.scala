package com.multiapps.routes

import spray.routing._
import spray.routing.directives.OnSuccessFutureMagnet._
import spray.httpx.marshalling.ToResponseMarshallable._
import spray.httpx.marshalling.ToResponseMarshaller._
import spray.httpx.marshalling.Marshaller._
import spray.routing.directives.FutureDirectives

import scala.concurrent.ExecutionContext
import scala.concurrent.Future

/**
  * Created by ydatta on 8/24/2016 AD.
  */
trait BaseRoute extends HttpService with FutureDirectives {
  import ExecutionContext.Implicits.global
  val requestUrl = extract(_.request.uri.toString)
  val body = extract{_.request.entity.asString}

  def serve(block: String => Future[String]): Route = {
    pathEnd(body(
      t => onSuccess(block(t)) { value =>
        complete(value)
      }
    ))
  }
}
