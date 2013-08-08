package microon.ri.activeobject

import microon.spi.scala.activeobject.ActiveObjectDispatcher
import java.util.concurrent.Future
import com.google.common.util.concurrent.Futures.immediateFuture

class ImmediateActiveObjectDispatcher extends ActiveObjectDispatcher {

  def dispatch[T](returnValue: => T): Future[T] =
    immediateFuture(returnValue)

}
