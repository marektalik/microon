package microon.ri.activeobject

import org.scalatest.FunSuite
import microon.spi.scala.activeobject.{MethodCall, DynamicClientMethodSupport}
import java.util.concurrent.Future
import com.google.common.util.concurrent.Futures.immediateFuture

class DynamicClientMethodServiceFactoryTest extends FunSuite {

  // Collaborators fixtures

  val clientService = DynamicClientMethodServiceFactory[ClientService](new DefaultGenericService).build

  // Data fixtures

  val argument = "argument"

  // Tests

  test("Should call generic operation.") {
    expectResult(argument) {
      clientService.genericMethod(argument).get
    }
  }

  test("Should call dynamic client operation.") {
    expectResult("clientMethod:" + argument) {
      clientService.clientMethod(argument).get
    }
  }

}

trait GenericService {

  def genericMethod(param: String): Future[String]

}

class DefaultGenericService extends GenericService with DynamicClientMethodSupport {

  activeObjectDispatcher(new ImmediateActiveObjectDispatcher)

  def genericMethod(param: String): Future[String] =
    immediateFuture(param)

  def dynamicMethodHandler = {
    case call: MethodCall => call.args.foldLeft(call.methodName)((accumulator, next) => accumulator + ":" + next)
  }

}

trait ClientService extends GenericService {

  def clientMethod(param: String): Future[String]

}
