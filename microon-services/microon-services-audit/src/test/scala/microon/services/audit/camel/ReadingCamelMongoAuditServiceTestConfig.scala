package microon.services.audit.camel

import org.springframework.scala.context.function.FunctionalConfiguration
import scalapi.embedmongo.EmbedMongoSupport
import org.springframework.data.mongodb.core.MongoTemplate
import microon.services.repository.impl.queryhandler.springdatamongoquery.SpringDataMongoQueryQueryHandler
import microon.services.audit.api.java.{AuditEvent => JAuditEvent}
import org.apache.camel.spring.SpringCamelContext
import org.springframework.context.ApplicationContext

object ReadingCamelMongoAuditServiceTestConfig extends FunctionalConfiguration with EmbedMongoSupport {

  val dbName = CamelMongoAuditService.defaultAuditDatabaseName

  val mongoClient = bean(name = "mongo")(mongo)

  val mongoTemplate = bean() {
    new MongoTemplate(mongo, dbName)
  }

  val queryHandler = bean() {
    SpringDataMongoQueryQueryHandler[JAuditEvent](mongoTemplate())
  }

  val camelContext = bean() {
    new SpringCamelContext(beanFactory.asInstanceOf[ApplicationContext])
  } init (_.start()) destroy (_.destroy())

  val auditService = bean()(new CamelMongoAuditService(camelContext()))

}
