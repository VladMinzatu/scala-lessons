package concurrency

import java.util.concurrent.Executors

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
 * The very basics of working with Futures
 */
object Futures01 extends App with ThreadUtils {
  implicit val ec = ExecutionContext.global

  val executor = Executors.newFixedThreadPool(2)
  val extraEc: ExecutionContext = ExecutionContext.fromExecutor(executor)

  println(s"Main thread: ${currentThread}")
  /**
   * Launches an asynchronous computation, producing a String result.
   * The computation will take place in the global execution context, implicitly passed here
   */
  val successfulFuture: Future[String] = Future {
    println(s"Feeling lucky in thread: ${currentThread}")
    Thread.sleep(500)
    "all good"
  }

  val failedFuture: Future[String] = Future {
    println(s"Feeling unlucky in thread: ${currentThread}")
    Thread.sleep(1000)
    throw new RuntimeException("something went terribly wrong")
  }

  def handleResult(future: Future[String]): Unit = {
    /**
     * Once the future is completed, asynchronously launch the given computation
     * (this is a callback)
     */
    future.onComplete{
      case Success(value) => println(s"[${currentThread}]This future says: $value")
      case Failure(throwable) => println(s"[${currentThread}]This future failed because ${throwable.getMessage}")
    }(extraEc) // We want this operation to be scheduled in a separate execution context, not the global one
    /** Note: If we're only interesting in doing something if the future is successful,
     * `foreach` is another handy callback. And if we're only interested in doing something
     * on failure, the `failed` projection can be used.
     */
  }

  handleResult(successfulFuture)
  handleResult(failedFuture)

  Thread.sleep(2000) // Stick around to see it

  // Terminate these non-daemon threads inside the executor to allow our App to exit.
  executor.shutdownNow()
}
