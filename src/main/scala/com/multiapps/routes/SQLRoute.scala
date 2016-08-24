package com.multiapps.routes

import com.multiapps.service.DemoService
import spray.routing._

/**
  * Created by ydatta on 8/24/2016 AD.
  */
trait SQLRoute extends BaseRoute with DemoService {
  def sqlRoutes(): Route = {
    sqlRoute
  }

  // Just one route
  val routes = sqlRoutes

  def sqlRoute: Route = pathPrefix("api" / "sql") {post { serve {  implicit sql: String => mainService } }}
}
