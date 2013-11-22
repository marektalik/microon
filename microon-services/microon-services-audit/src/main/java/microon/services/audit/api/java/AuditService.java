package microon.services.audit.api.java;

import microon.spi.scala.activeobject.Void;

import java.util.concurrent.Future;

public interface AuditService {

    Future<Void> log(AuditEvent auditEvent);

}
