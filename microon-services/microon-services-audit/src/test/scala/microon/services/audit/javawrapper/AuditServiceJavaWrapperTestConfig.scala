package microon.services.audit.javawrapper

import org.springframework.scala.context.function.FunctionalConfiguration
import scalapi.embedmongo.EmbedMongoSupport
import org.springframework.data.mongodb.core.MongoTemplate
import microon.services.audit.api.java.{AuditEvent => JAuditEvent}
import org.apache.camel.spring.SpringCamelContext
import org.springframework.context.ApplicationContext
import microon.services.audit.camel.CamelMongoAuditService

object AuditServiceJavaWrapperTestConfig extends FunctionalConfiguration with EmbedMongoSupport {

  val dbName = CamelMongoAuditService.defaultAuditDatabaseName

  val mongoClient = bean(name = "mongo")(mongo)

  val mongoTemplate = bean() {
    new MongoTemplate(mongo, dbName)
  }

  val camelContext = bean() {
    new SpringCamelContext(beanFactory.asInstanceOf[ApplicationContext])
  } init (_.start()) destroy (_.destroy())

  val auditService = bean()(new CamelMongoAuditService(camelContext()))

  val javaAuditService = bean()(new AuditServiceJavaWrapper(auditService()))

}
