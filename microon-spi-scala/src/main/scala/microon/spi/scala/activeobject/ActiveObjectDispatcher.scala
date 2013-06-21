package microon.spi.scala.activeobject

import java.util.concurrent.Future

trait ActiveObjectDispatcher {

  def dispatch[T](returnValue: => T): Future[T]

}