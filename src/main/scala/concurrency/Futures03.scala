package concurrency

import scala.concurrent.{ExecutionContext, Future}

/**
 * Building on Futures02, let's look at how functional composition and for-comprehension apply to Futures
 */
object Futures03 extends App with ThreadUtils {

  implicit val ec = ExecutionContext.global

  /**
   * Futures also support flatMap (which maps the value to a new Future) and withFilter
   */
  val helloFuture = Future {
    println(s"[$currentThread]Will say hello in just a sec")
    Thread.sleep(1000)
    "Hello"
  }

  val worldFuture = Future {
    println(s"[$currentThread]Will say world in just a sec")
    Thread.sleep(1000)
    "World"
  }

  /**
   * This will translate the usual way for-comprehensions do.
   */
  val result = for{
    hello <- helloFuture
    world <- worldFuture
    if(world.length > 2)
  } yield s"$hello, $world"

  result.foreach{
    s => println(s)
  }

  Thread.sleep(2000) // Stick around to see it

  /**
   * Notice that because the futures were created before the for block, they are triggered concurrently
   * and the result will be ready in roughly one second.
   * Contrast this with:
   */
  val bonjourFuture = Future{
    println(s"[$currentThread]Will say bonjour in just a sec")
    Thread.sleep(1000)
    "Bonjour"
  }

  val frenchResult = for{
    bonjour <- bonjourFuture
    leMonde <- Future{
      println(s"[$currentThread]Will say le monde in just a sec")
      Thread.sleep(1000)
      "le monde"
      }
    } yield s"$bonjour, $leMonde"

  frenchResult.foreach{
    s => println(s)
  }

  Thread.sleep(3000) // Stick around to see it
  /**
   * Now the second future is only created after the first one completes, so the whole thing takes roughly 2 seconds.
   * So while for-comprehension may improve readability, don't lose sight of what it actually translates to and what
   * is going on under the hood!
   */
}
