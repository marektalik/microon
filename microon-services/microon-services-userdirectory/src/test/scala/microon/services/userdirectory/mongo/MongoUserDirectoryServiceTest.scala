package microon.services.userdirectory.mongo

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import org.springframework.data.mongodb.core.MongoTemplate
import com.mongodb.MongoClient
import org.springframework.scala.context.function.{ContextSupport, FunctionalConfiguration}
import scala.Some
import microon.services.userdirectory.{UserDirectoryService, User}
import microon.ri.boot.spring.scala.SpringScalaBoot
import scalapi.embedmongo.EmbedMongoSupport

@RunWith(classOf[JUnitRunner])
class MongoUserDirectoryServiceTest extends FunSuite with BeforeAndAfter with BeforeAndAfterAll
with EmbedMongoSupport {

  // Services fixtures

  val boot = SpringScalaBoot[TestConfig].start()
  var service = boot.context[UserDirectoryService]

  override def afterAll(configMap: Map[String, Any]) {
    stopMongo()
  }

  before {
    mongo.dropDatabase(MongoUserDirectoryService.userDirectoryDBName)
  }

  // User existence

  test("Should not find non-existing user.") {
    expectResult(false) {
      val randomUserId = "123456789012345678901234"
      service.userExists(randomUserId).get
    }
  }

  test("Should find existing user.") {
    val userId = service.createUser().get
    expectResult(true) {
      service.userExists(userId).get
    }
  }

  // Properties access

  test("Should load user properties.") {
    val userId = service.createUser(Map("foo" -> "bar")).get
    expectResult(Map("foo" -> "bar")) {
      service.loadUserProperties(userId).get
    }
  }

  test("Should load user property.") {
    val userId = service.createUser(Map("foo" -> "bar")).get
    expectResult(Some("bar")) {
      service.loadUserProperty(userId, "foo").get
    }
  }

  test("Should return None for non-existing user property.") {
    val userId = service.createUser().get
    expectResult(None) {
      service.loadUserProperty(userId, "randomProperty").get
    }
  }

  test("Should load user id by property.") {
    val userId = service.createUser(Map("foo" -> "bar")).get
    expectResult(Some(userId)) {
      service.loadUserIdByProperty("foo", "bar").get
    }
  }

  test("Should list users properties.") {
    val properties = Map("foo" -> "bar", "baz" -> "qux")
    val user1Id = service.createUser(properties).get
    val user2Id = service.createUser(properties).get
    expectResult(Seq(User(user1Id, Map("foo" -> "bar")), User(user2Id, Map("foo" -> "bar")))) {
      service.listUsersProperties(Seq("foo")).get
    }
  }

  test("Should update user properties.") {
    val userId = service.createUser(Map("foo" -> "bar")).get
    expectResult(Map("baz" -> "qux", "foo" -> "bar")) {
      service.updateUserProperties(userId, Map("baz" -> "qux")).get
      service.loadUserProperties(userId).get
    }
  }

}

class TestConfig extends FunctionalConfiguration with ContextSupport {
  enableAnnotationConfig()

  val mongoTemplate = bean() {
    new MongoTemplate(new MongoClient, MongoUserDirectoryService.userDirectoryDBName)
  }

  bean()(new MongoUserDirectoryService(mongoTemplate()))

}