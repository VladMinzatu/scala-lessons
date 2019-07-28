package concurrency

trait ThreadUtils {
  def currentThread: String = Thread.currentThread.getName
}
