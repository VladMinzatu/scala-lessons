package algorithms.dynamic_programming

class Fibonacci extends Memoization {

  lazy val fib: Int => Long = {
    memoize {
      case 0 => 0
      case 1 => 1
      case n => {println(s"Computing fib($n)"); fib(n - 1) + fib(n - 2)}
    }
  }

  def get(n: Int): Long = {
    fib(n)
  }
}

object Fibonacci extends App {
  val f = new Fibonacci()
  println(f.get(9))
  for(i <- 10 to 20) println(f.get(i))
}
