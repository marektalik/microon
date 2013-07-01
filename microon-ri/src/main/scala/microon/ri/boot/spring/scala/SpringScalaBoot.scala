package microon.ri.boot.spring.scala

import org.springframework.scala.context.function.{ContextSupport, FunctionalConfigApplicationContext, FunctionalConfiguration}
import microon.ri.boot.Boot
import java.util.concurrent.Executors._
import microon.ri.activeobject.ExecutorServiceActiveObjectDispatcher
import scala.reflect.ClassTag
import scala.reflect.classTag

class SpringScalaBoot(configClasses: Class[_ <: FunctionalConfiguration]*) extends Boot {

  val context = FunctionalConfigApplicationContext[BootConfig]
  context.registerClasses(configClasses: _*)

  def start(): SpringScalaBoot = {
    context.start()
    this
  }

  def stop() {
    context.stop()
  }

}

object SpringScalaBoot {

  def apply[T <: FunctionalConfiguration : ClassTag] =
    new SpringScalaBoot(classTag[T].runtimeClass.asInstanceOf[Class[T]])

}

private class BootConfig extends FunctionalConfiguration with ContextSupport {
  enableAnnotationConfig()

  val executor = bean()(newCachedThreadPool)
  bean()(new ExecutorServiceActiveObjectDispatcher(executor()))
}