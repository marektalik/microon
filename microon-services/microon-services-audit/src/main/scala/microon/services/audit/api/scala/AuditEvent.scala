package microon.services.audit.api.scala

case class AuditEvent(message: String, context: Map[String, String], tags: Seq[String])
