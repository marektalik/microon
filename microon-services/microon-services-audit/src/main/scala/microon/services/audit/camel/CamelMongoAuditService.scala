package microon.services.audit.camel

import microon.services.audit.api.scala.{AuditEvent, AuditService}
import java.util.concurrent.Future
import microon.spi.scala.activeobject.Void
import org.apache.camel.CamelContext
import org.apache.camel.scala.dsl.builder.RouteBuilder
import com.google.common.util.concurrent.Futures.immediateFuture
import microon.services.audit.api.java.{AuditEvent => JAuditEvent}
import scala.collection.JavaConversions._
import CamelMongoAuditService._

class CamelMongoAuditService(camelContext: CamelContext, auditDatabaseName: String = defaultAuditDatabaseName) extends AuditService {

  val inputEndpoint = "seda:auditService?failIfNoConsumers=true&concurrentConsumers=3"

  camelContext.addRoutes(new RouteBuilder {
    inputEndpoint --> "mongodb:mongo?database=%s&collection=auditEvent&operation=insert".format(auditDatabaseName)
  })

  def log(auditEvent: AuditEvent): Future[Void] = {
    val javaEvent = new JAuditEvent(auditEvent.id.orNull, auditEvent.message, auditEvent.context, auditEvent.tags.toArray)
    camelContext.createProducerTemplate().sendBody(inputEndpoint, javaEvent)
    immediateFuture(Void())
  }

}

object CamelMongoAuditService {

  val defaultAuditDatabaseName = "auditLog"

}