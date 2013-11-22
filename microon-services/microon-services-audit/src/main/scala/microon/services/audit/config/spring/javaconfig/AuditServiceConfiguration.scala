package microon.services.audit.config.spring.javaconfig

import org.springframework.context.annotation.{Bean, Configuration}
import microon.services.audit.camel.CamelMongoAuditService
import com.mongodb.MongoClient
import org.springframework.beans.factory.annotation.Autowired
import org.apache.camel.CamelContext
import microon.services.audit.api.scala.AuditService
import microon.services.audit.api.java.{AuditService => JAuditService}
import microon.services.audit.javawrapper.AuditServiceJavaWrapper

@Configuration
class AuditServiceConfiguration {

  @Autowired
  var mongo: MongoClient = _

  @Autowired
  var camelContext: CamelContext = _

  @Bean
  def auditServiceDbName: String =
    CamelMongoAuditService.defaultAuditDatabaseName

  @Bean
  def auditService: AuditService =
    new CamelMongoAuditService(camelContext)

  @Bean
  def javaAuditService: JAuditService =
    new AuditServiceJavaWrapper(auditService)

}