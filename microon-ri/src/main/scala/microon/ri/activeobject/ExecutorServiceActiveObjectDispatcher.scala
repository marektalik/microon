package microon.ri.activeobject

import javax.inject.Inject
import java.util.concurrent.{Callable, Future, ExecutorService}
import microon.spi.scala.activeobject.ActiveObjectDispatcher

class ExecutorServiceActiveObjectDispatcher(@Inject executor: ExecutorService) extends ActiveObjectDispatcher {

  def dispatch[T](returnValue: => T): Future[T] =
    executor.submit(new Callable[T] {
      def call = returnValue
    })

}