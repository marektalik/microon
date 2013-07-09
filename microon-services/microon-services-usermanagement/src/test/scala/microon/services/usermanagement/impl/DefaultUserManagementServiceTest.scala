package microon.services.usermanagement.impl

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfter, FunSuite}
import microon.ri.boot.spring.scala.SpringScalaBoot
import org.springframework.scala.context.function.{ContextSupport, FunctionalConfiguration}
import org.springframework.data.mongodb.core.MongoTemplate
import com.mongodb.MongoClient
import microon.services.userdirectory.mongo.MongoUserDirectoryService
import microon.services.usermanagement.{User, UserManagementService}
import scalapi.embedmongo.EmbedMongoSupport
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class DefaultUserManagementServiceTest extends FunSuite with BeforeAndAfter with BeforeAndAfterAll
with EmbedMongoSupport {

  // Services fixtures

  val boot = SpringScalaBoot[TestConfig].start()
  var service = boot.context[UserManagementService]

  override def afterAll(configMap: Map[String, Any]) {
    stopMongo()
  }

  before {
    mongo.dropDatabase(MongoUserDirectoryService.userDirectoryDBName)
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

  val mongoTemplate = bean() {
    new MongoTemplate(new MongoClient, MongoUserDirectoryService.userDirectoryDBName)
  }

  val directoryService = bean()(new MongoUserDirectoryService(mongoTemplate()))


  bean()(new DefaultUserManagementService(directoryService(), Seq("foo", "bar")))

}