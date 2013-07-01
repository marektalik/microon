package microon.spi.scala.activeobject

import java.util.concurrent.Future

/**
 * Dispatches the return value of the service (active object) operation.
 */
trait ActiveObjectDispatcher {

  /**
   * Dispatches the return value of the service operation.
   *
   * @param returnValue value returned by the service operation
   * @tparam T type of the return value
   * @return return value of the service operation wrapped into [[java.util.concurrent.Future]] and scheduled for being
   *         dispatched
   */
  def dispatch[T](returnValue: => T): Future[T]

}