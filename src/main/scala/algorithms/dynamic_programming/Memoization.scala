package algorithms.dynamic_programming

import scala.collection.mutable

trait Memoization {

  def memoize[I, O](f: I => O): I => O = new mutable.HashMap[I, O]() {
    override def apply(key: I) = getOrElseUpdate(key, f(key))
  }
}
