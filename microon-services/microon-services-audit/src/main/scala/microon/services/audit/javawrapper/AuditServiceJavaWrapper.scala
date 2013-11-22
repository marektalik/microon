package microon.services.audit.javawrapper

import microon.services.audit.api.java.{AuditEvent => JAuditEvent}
import microon.services.audit.api.java.{AuditService => JAuditService}
import java.util.concurrent.Future
import microon.spi.scala.activeobject.Void
import microon.services.audit.api.scala.{AuditEvent, AuditService}
import scala.collection.JavaConversions._

class AuditServiceJavaWrapper(auditService: AuditService) extends JAuditService {

  def log(auditEvent: JAuditEvent): Future[Void] =
    auditService.log(AuditEvent(Option(auditEvent.getId), auditEvent.getMessage, auditEvent.getContext.toMap, auditEvent.getTags))

}
