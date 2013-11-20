package microon.services.audit.api.scala

case class AuditEvent(id: Option[String], message: String, context: Map[String, String], tags: Seq[String])
