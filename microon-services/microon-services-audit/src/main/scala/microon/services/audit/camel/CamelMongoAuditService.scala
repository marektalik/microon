package microon.services.audit.camel

import microon.services.audit.api.scala.{AuditEvent, AuditService}
import java.util.concurrent.Future
import microon.spi.scala.activeobject.Void
import org.apache.camel.CamelContext
import org.apache.camel.scala.dsl.builder.RouteBuilder
import com.google.common.util.concurrent.Futures.immediateFuture
import microon.services.audit.api.java.{AuditEvent => JavaAuditEvent}
import scala.collection.JavaConversions._

class CamelMongoAuditService(camelContext: CamelContext) extends AuditService {

  val inputEndpoint = "seda:auditService?failIfNoConsumers=true"

  camelContext.addRoutes(new RouteBuilder {
    inputEndpoint --> "mongodb:mongo?database=audit&collection=auditEvent&operation=insert"
  })

  def log(auditEvent: AuditEvent): Future[Void] = {
    val javaEvent = new JavaAuditEvent(auditEvent.message, auditEvent.context, auditEvent.tags.toArray)
    camelContext.createProducerTemplate().asyncSendBody(inputEndpoint, javaEvent)
    immediateFuture(Void())
  }

}