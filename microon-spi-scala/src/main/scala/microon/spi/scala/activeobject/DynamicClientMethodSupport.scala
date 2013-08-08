package microon.spi.scala.activeobject

import java.util.concurrent.Future

trait DynamicClientMethodSupport extends ActiveObject {

  def dynamicMethodHandler: PartialFunction[MethodCall, Any]

  private[microon] def dispatchDynamicMethod[T](call: MethodCall): Future[Any] =
    dispatch(dynamicMethodHandler(call))

}

case class MethodCall(methodName: String, args: Array[Any])
