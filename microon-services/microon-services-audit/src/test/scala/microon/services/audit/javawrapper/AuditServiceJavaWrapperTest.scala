package microon.services.audit.javawrapper

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{Matchers, BeforeAndAfter, FunSuite}
import scalapi.jdk.control.Try
import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import microon.services.audit.api.java.{AuditEvent => JAuditEvent}
import org.springframework.data.mongodb.core.query.Query
import AuditServiceJavaWrapperTestConfig._
import scala.collection.JavaConversions._

@RunWith(classOf[JUnitRunner])
class AuditServiceJavaWrapperTest extends FunSuite with BeforeAndAfter with Matchers {

  // Collaborators fixtures

  val ctx = new FunctionalConfigApplicationContext()
  ctx.registerConfigurations(AuditServiceJavaWrapperTestConfig)

  // Data fixtures

  val event = new JAuditEvent(null, "message", Map("key" -> "value"), Array("tag1"))

  before {
    mongoClient().dropDatabase(dbName)
    javaAuditService().log(event)
  }

  // Tests

  test("Should store audit event.") {
    assertResult(1) {
      Try times 20 until {
        mongoTemplate().count(new Query(), classOf[JAuditEvent]) > 0
      } match {
        case None => throw new IllegalStateException("Cannot find audit events in Mongo.")
        case _ => mongoTemplate().count(new Query(), classOf[JAuditEvent])
      }
    }
  }

}