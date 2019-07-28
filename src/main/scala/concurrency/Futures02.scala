package concurrency

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

/**
 * In Futures01.scala we scheduled asynchronous processing of a Future's result.
 * But these can be chained using map
 */
object Futures02 extends App with ThreadUtils {
  implicit val ec = ExecutionContext.global

  /**
   * Like onComplete, map also allows us to specify some asynchronous transformation on the result of a future.
   * This will produce a new Future, thus allowing us to chain operations.
   */
  val initialFuture: Future[String] = Future {
    println(s"[$currentThread] Producing a string representation of 1")
    Thread.sleep(500)
    "1"
  }

  val parsedFuture: Future[Int] = initialFuture.map{ s =>
    println(s"[$currentThread] Parsing the string as an Int")
    Thread.sleep(500)
    s.toInt // this may fail in general
  }

  /**
   * But map is only concerned with the happy path:
   * This will not be scheduled if the previous future failed
   */
  val multipliedFuture: Future[Int] = parsedFuture.map{ i =>
    println(s"[$currentThread] Multiplying the Int by 100")
    Thread.sleep(500)
    i * 100
  }

  // Waiting for the end result. DO NOT do this in production
  val result = Await.result(multipliedFuture, 2.seconds)

  println(s"End result: $result")
  /**
   * So essentially, the operations took place sequentially, but they were scheduled independently.
   * We can, of course, apply two different transformations concurrently on the result of one Future.
   */
}
