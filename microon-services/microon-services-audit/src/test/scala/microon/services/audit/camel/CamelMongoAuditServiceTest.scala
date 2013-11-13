package microon.services.audit.camel

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.apache.camel.impl.{SimpleRegistry, DefaultCamelContext}
import microon.services.audit.api.scala.AuditEvent
import scalapi.embedmongo.EmbedMongoSupport
import scala.collection.JavaConversions._

@RunWith(classOf[JUnitRunner])
class CamelMongoAuditServiceTest extends FunSuite with BeforeAndAfter with EmbedMongoSupport {

  // Collaborators fixtures

  val registry = new SimpleRegistry
  registry.put("mongo", mongo)
  val camelContext = new DefaultCamelContext(registry)

  val auditService = new CamelMongoAuditService(camelContext)

  camelContext.start()

  // Data fixtures

  def auditDb = mongo.getDB("audit")

  val event = AuditEvent("message", Map.empty, Seq("tag1"))

  before {
    auditDb.dropDatabase()
  }

  // Tests

  test("Should store audit event.") {
    expectResult(1) {
      auditService.log(event)
      Try times 20 until {
        auditDb.getCollection("auditEvent").count > 0
      }
      auditDb.getCollection("auditEvent").count
    }
  }

  test("Should store audit event message.") {
    expectResult(event.message) {
      auditService.log(event)
      Try times 20 until {
        auditDb.getCollection("auditEvent").count > 0
      }
      auditDb.getCollection("auditEvent").findOne().get("message")
    }
  }

  test("Should store audit event tags.") {
    expectResult(event.tags) {
      auditService.log(event)
      Try times 20 until {
        auditDb.getCollection("auditEvent").count > 0
      }
      auditDb.getCollection("auditEvent").findOne().get("tags").asInstanceOf[java.util.List[String]].toSeq
    }
  }

  // Helpers

  /**
   * @deprecated on behalf of scalapi.jdk.control.Try (Scalapi 0.6)
   */
  object Try {

    def times(count: Int) =
      TimesClause(count)

    case class TimesClause(initialCount: Int) {

      def until(condition: => Boolean): Option[Int] =
        until(initialCount)(condition)

      private def until(count: Int)(condition: => Boolean): Option[Int] =
        (count, condition) match {
          case (c, _) if c == 0 => None
          case (c, cond) if cond => Some(c)
          case (c, _) => {
            Thread.sleep(100)
            until(c - 1)(condition)
          }
        }

    }

  }

}