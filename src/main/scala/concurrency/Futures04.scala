package concurrency

import scala.concurrent.{ExecutionContext, Future}

/**
 * Recovering from failure
 */
object Futures04 extends App {
  implicit val ec = ExecutionContext.global

  /**
   * The recover combinator creates a new future which either returns the original result
   * or the result of applying the given operation in case of failure
   */
  val successfulFuture: Future[Int] = Future {
    Thread.sleep(1000)
    1
  }

  val failedFuture: Future[Int] = Future {
    Thread.sleep(1000)
    throw new RuntimeException
  }

  def recover(future: Future[Int]): Future[Int] = {
    future.recover {
      case _ => 0
    }
  }

  recover(successfulFuture).foreach{ value =>
    println(s"Successful value: $value")
  }

  recover(failedFuture).foreach{ value =>
    println(s"Failed value: $value")
  }

  Thread.sleep(2000) // Stick around to see it
}
