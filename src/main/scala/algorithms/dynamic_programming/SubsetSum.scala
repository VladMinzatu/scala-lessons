package algorithms.dynamic_programming

// determine whether there is a
class SubsetSum(arr: Array[Int], sum: Int) extends Memoization {

  // maps (idx, sum) to whether there is a solution
  // to SubsetSum for arr[:idx, sum]
  lazy val ss: ((Int, Int)) => Boolean = memoize {
      case (_, 0) => true
      case (0, s) => arr(0) == s
      case (idx, s) if arr(idx) > s => ss(idx - 1, s)
      case (idx, s) => ss(idx - 1, s) || ss(idx - 1, s - arr(idx))
    }

  def get: Boolean = ss(arr.length - 1, sum)
}

object SubsetSum extends App {
  println(new SubsetSum(Array(4,1,10,12,5,2), 9).get)
  println(new SubsetSum(Array(1,8,2,5), 4).get)
}