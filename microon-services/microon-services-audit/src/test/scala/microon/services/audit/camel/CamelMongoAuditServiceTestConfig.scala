package microon.services.audit.camel

import org.springframework.scala.context.function.FunctionalConfiguration
import scalapi.embedmongo.EmbedMongoSupport
import org.springframework.data.mongodb.core.MongoTemplate
import microon.services.repository.impl.queryhandler.springdatamongoquery.SpringDataMongoQueryQueryHandler
import microon.services.audit.api.java.{AuditEvent => JAuditEvent}
import org.apache.camel.spring.SpringCamelContext
import org.springframework.context.ApplicationContext
import microon.services.audit.config.spring.javaconfig.AuditServiceConfiguration
import microon.services.audit.api.scala.AuditService
import microon.services.audit.api.java.{AuditService => JAuditService}


object CamelMongoAuditServiceTestConfig extends FunctionalConfiguration with EmbedMongoSupport {

  importClass(classOf[AuditServiceConfiguration])

  lazy val auditServiceDbName = getBean[String]("auditServiceDbName")

  lazy val auditService = getBean[AuditService]("auditService")

  lazy val javaAuditService = getBean[JAuditService]("javaAuditService")

  val mongoClient = bean(name = "mongo")(mongo)

  val mongoTemplate = bean() {
    new MongoTemplate(mongo, auditServiceDbName)
  }

  val camelContext = bean() {
    new SpringCamelContext(beanFactory.asInstanceOf[ApplicationContext])
  } init (_.start()) destroy (_.destroy())

  val queryHandler = bean() {
    SpringDataMongoQueryQueryHandler[JAuditEvent](mongoTemplate())
  }

}
