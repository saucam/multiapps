package com.multiapps

import scalaz.concurrent.Task

/**
  * Created by ydatta on 8/16/2016 AD.
  */
object TaskFactory {

  /**
    * Returns the task by wrapping the given closure computation in a task
    *
    * @param a - Computation to be performed by the task
    * @tparam A - Desired Ouput Type
    * @return - Scalaz's Task
    */
  def getTask[A](a: => A): Task[A] = Task(a)

}
