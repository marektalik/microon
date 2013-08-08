package microon.ri.activeobject

import microon.spi.scala.activeobject.{MethodCall, DynamicClientMethodSupport}
import java.lang.reflect.Proxy._
import java.lang.reflect.{Method, InvocationHandler}
import scala.reflect.ClassTag
import scalapi.jdk.reflect.Tags
import Tags._

class DynamicClientMethodServiceFactory[CS](targetService: DynamicClientMethodSupport, clientService: Class[CS]) {

  def build: CS =
    newProxyInstance(getClass.getClassLoader, Array(clientService), new InvocationHandler {
      def invoke(proxy: Any, method: Method, args: Array[AnyRef]): AnyRef = {
        if (method.getDeclaringClass.isAssignableFrom(targetService.getClass)) {
          method.invoke(targetService, args: _*)
        } else {
          targetService.dispatchDynamicMethod(MethodCall(method.getName, args.asInstanceOf[Array[Any]]))
        }
      }
    }).asInstanceOf[CS]

}

object DynamicClientMethodServiceFactory {

  def apply[CS: ClassTag](targetService: DynamicClientMethodSupport): DynamicClientMethodServiceFactory[CS] =
    new DynamicClientMethodServiceFactory(targetService, typeToClass[CS])

}