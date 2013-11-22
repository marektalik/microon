package microon.services.audit.camel

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import scalapi.jdk.control.Try
import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import microon.services.audit.camel.CamelMongoAuditServiceTestConfig._
import org.springframework.data.mongodb.core.query.Query
import microon.services.audit.api.java.{AuditEvent => JAuditEvent}
import microon.services.audit.api.scala.AuditEvent

@RunWith(classOf[JUnitRunner])
class CamelMongoAuditServiceTest extends FunSuite with BeforeAndAfter {

  // Collaborators fixtures

  val ctx = new FunctionalConfigApplicationContext()
  ctx.registerConfigurations(CamelMongoAuditServiceTestConfig)
  ctx.refresh()

  // Data fixtures

  val event = AuditEvent(None, "message", Map.empty, Seq("tag1"))

  before {
    mongoClient().dropDatabase(auditServiceDbName)
    auditService.log(event)
  }

  // Tests

  test("Should store audit event.") {
    assertResult(1) {
      Try times 20 until {
        mongoTemplate().count(new Query(), classOf[JAuditEvent]) > 0
      }
      mongoTemplate().count(new Query(), classOf[JAuditEvent])
    }
  }

  test("Should store audit event message.") {
    assertResult(event.message) {
      Try times 20 until {
        mongoTemplate().count(new Query(), classOf[JAuditEvent]) > 0
      }
      mongoTemplate().findOne(new Query(), classOf[JAuditEvent]).getMessage
    }
  }

  test("Should store audit event tags.") {
    assertResult(event.tags) {
      Try times 20 until {
        mongoTemplate().count(new Query(), classOf[JAuditEvent]) > 0
      }
      mongoTemplate().findOne(new Query(), classOf[JAuditEvent]).getTags.toSeq
    }
  }

}