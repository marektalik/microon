package microon.services.audit.api.scala

import java.util.concurrent.Future
import microon.spi.scala.activeobject.Void

trait AuditService {

  def log(auditEvent: AuditEvent): Future[Void]

}