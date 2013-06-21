package microon.spi.scala.activeobject

import org.scalatest.{BeforeAndAfter, FunSuite}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.util.concurrent.Executors.newCachedThreadPool
import org.springframework.scala.context.function.{ContextSupport, FunctionalConfiguration, FunctionalConfigApplicationContext}
import java.util.concurrent.Future
import microon.spi.scala.activeobject

@RunWith(classOf[JUnitRunner])
class ActiveObjectTest extends FunSuite with BeforeAndAfter {

  var service: DefaultTestService = _

  before {
    val context = FunctionalConfigApplicationContext[TestConfig]
    service = context.bean[DefaultTestService].get
  }

  test("Should dispatch void operation.") {
    expectResult(true) {
      service.voidOperation.get
      service.voidOperationExecuted
    }
  }

  test("Should dispatch non void operation.") {
    expectResult(service.result) {
      service.nonVoidOperation.get
    }
  }

}

class TestConfig extends FunctionalConfiguration with ContextSupport {
  enableAnnotationConfig()

  val executor = bean()(newCachedThreadPool)
  bean()(new ExecutorServiceActiveObjectDispatcher(executor()))
  bean()(new DefaultTestService)
}

trait TestService {
  def voidOperation: Future[activeobject.Void]

  def nonVoidOperation: Future[String]
}

class DefaultTestService extends TestService with ActiveObject {

  val result = "result"

  var voidOperationExecuted = false

  def voidOperation: Future[activeobject.Void] = void {
    voidOperationExecuted = true
  }

  def nonVoidOperation: Future[String] = dispatch {
    result
  }

}