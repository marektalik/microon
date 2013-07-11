package microon.services.usermanagement.impl

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfter, FunSuite}
import microon.ri.boot.spring.scala.SpringScalaBoot
import org.springframework.scala.context.function.{ContextSupport, FunctionalConfiguration}
import org.springframework.data.mongodb.core.MongoTemplate
import microon.services.userdirectory.mongo.MongoUserDirectoryService
import microon.services.usermanagement.{User, UserManagementService}
import scalapi.embedmongo.EmbedMongoServer
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class DefaultUserManagementServiceTest extends FunSuite with BeforeAndAfter with BeforeAndAfterAll {

  // Services fixtures

  val boot = SpringScalaBoot[TestConfig].start()
  val mongoServer = boot.context[EmbedMongoServer]
  var service = boot.context[UserManagementService]

  override def afterAll(configMap: Map[String, Any]) {
    mongoServer.stop()
  }

  before {
    mongoServer.client.dropDatabase(MongoUserDirectoryService.userDirectoryDBName)
  }

  // Data fixtures

  val username = "Henry"

  // Tests

  test("Should not find user.") {
    expectResult(false) {
      service.userExists("randomUsername").get
    }
  }

  test("Should find user.") {
    service.registerUser(username).get
    assert(service.userExists(username).get)
  }

  test("Should load user.") {
    val id = service.registerUser(username).get
    expectResult(User(id, Map("username" -> username))) {
      service.loadUser(id).get
    }
  }

}

class TestConfig extends FunctionalConfiguration with ContextSupport {
  enableAnnotationConfig()

  val mongoServer = bean() {
    new EmbedMongoServer
  }

  val mongoTemplate = bean() {
    new MongoTemplate(mongoServer().client, MongoUserDirectoryService.userDirectoryDBName)
  }

  val directoryService = bean()(new MongoUserDirectoryService(mongoTemplate()))


  bean()(new DefaultUserManagementService(directoryService(), Seq("foo", "bar")))

}