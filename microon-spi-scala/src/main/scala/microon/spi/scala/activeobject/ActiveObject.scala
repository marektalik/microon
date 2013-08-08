package microon.spi.scala.activeobject

import java.util.concurrent.Future
import javax.inject.Inject

/**
 * Provides support for dispatching method execution according to the Active Object pattern.
 */
trait ActiveObject {

  /**
   * [[microon.spi.scala.activeobject.ActiveObjectDispatcher]] used to delegate the execution of the method.
   */
  @Inject private var activeObjectDispatcher: ActiveObjectDispatcher = _

  protected def activeObjectDispatcher(activeObjectDispatcher: ActiveObjectDispatcher) {
    this.activeObjectDispatcher = activeObjectDispatcher
  }

  /**
   * Method called by Active Object service in order to dispatch the result of the executed operation.
   *
   * @param returnValue return value of the executed operation to be dispatched
   * @tparam T type of operation result
   * @return result of the executed operation wrapped into [[java.util.concurrent.Future]]
   */
  protected def dispatch[T](returnValue: => T): Future[T] =
    activeObjectDispatcher.dispatch(returnValue)

  protected def void(returnValue: => Unit): Future[Void] = dispatch {
    returnValue
    Void()
  }

}

case class Void()