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
  @Inject private[activeobject] var activeObjectDispatcher: ActiveObjectDispatcher = _

  protected def dispatch[T](returnValue: => T): Future[T] =
    activeObjectDispatcher.dispatch(returnValue)

  protected def void(returnValue: => Unit): Future[Void] = dispatch {
    returnValue
    Void()
  }

}

case class Void()