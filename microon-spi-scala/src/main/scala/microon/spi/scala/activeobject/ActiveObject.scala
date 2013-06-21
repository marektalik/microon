package microon.spi.scala.activeobject

import java.util.concurrent.Future
import javax.inject.Inject

trait ActiveObject {

  @Inject
  private[activeobject] var activeObjectDispatcher: ActiveObjectDispatcher = _

  def dispatch[T](returnValue: => T): Future[T] =
    activeObjectDispatcher.dispatch(returnValue)

  def void(returnValue: => Unit): Future[Void] = dispatch {
    returnValue
    Void()
  }

}

case class Void()



