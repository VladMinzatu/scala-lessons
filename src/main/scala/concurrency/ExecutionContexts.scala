package concurrency

import scala.concurrent.{ExecutionContext, Future, blocking}

object ExecutionContexts extends App {

  /**
   * The global execution context - a global static thread pool with parallelism level equal to Runtime.availableProcessors
   * But configurable through scala.concurrent.context.minThreads, scala.concurrent.context.numThreads,
   * scala.concurrent.context.maxThreads
   */
  implicit val ec = ExecutionContext.global

  /**
   * blocking operations can cause the number of concurrently blocking computations to exceed the parallelism level
   * Behold, many threads!
   */
  for(i <- 1 to 50){
    Future {
      blocking {
        println(s"Thread ${Thread.currentThread().getName} spawned.")
        Thread.sleep(30_000)
      }
    }
  }
}
