package microon.services.audit.camel

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import microon.services.audit.api.scala.AuditEvent
import scalapi.jdk.control.Try
import org.springframework.scala.context.function.FunctionalConfigApplicationContext
import microon.services.audit.api.java.{AuditEvent => JAuditEvent}
import org.springframework.data.mongodb.core.query.Query
import org.scalatest.matchers.ShouldMatchers
import scala.collection.JavaConversions._
import ReadingCamelMongoAuditServiceTestConfig._

@RunWith(classOf[JUnitRunner])
class ReadingCamelMongoAuditServiceTest extends FunSuite with BeforeAndAfter with ShouldMatchers {

  // Collaborators fixtures

  val ctx = new FunctionalConfigApplicationContext()
  ctx.registerConfigurations(ReadingCamelMongoAuditServiceTestConfig)

  // Data fixtures

  val event = AuditEvent(None, "message", Map("key" -> "value"), Seq("tag1"))

  before {
    mongoClient().dropDatabase(dbName)
    auditService().log(event)
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

  test("Should read audit event.") {
    assertResult(1) {
      Try times 20 until {
        mongoTemplate().count(new Query(), classOf[JAuditEvent]) > 0
      } match {
        case None => throw new IllegalStateException("Cannot find audit events in Mongo.")
        case _ => mongoTemplate().count(new Query(), classOf[JAuditEvent])
      }
      queryHandler().countByQuery(new Query())
    }
  }

  test("Should load audit event id.") {
    Try times 20 until {
      mongoTemplate().count(new Query(), classOf[JAuditEvent]) > 0
    } match {
      case None => throw new IllegalStateException("Cannot find audit events in Mongo.")
      case _ => mongoTemplate().count(new Query(), classOf[JAuditEvent])
    }
    queryHandler().findOneByQuery(new Query()).getId should not be null
  }

  test("Should load audit event context.") {
    assertResult(event.context) {
      Try times 20 until {
        mongoTemplate().count(new Query(), classOf[JAuditEvent]) > 0
      } match {
        case None => throw new IllegalStateException("Cannot find audit events in Mongo.")
        case _ => mongoTemplate().count(new Query(), classOf[JAuditEvent])
      }
      queryHandler().findOneByQuery(new Query()).getContext.toMap
    }
  }

}

