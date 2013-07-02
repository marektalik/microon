package microon.services.userdirectory.impl

import org.scalatest.{BeforeAndAfterAll, BeforeAndAfter, FunSuite}
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import de.flapdoodle.embed.mongo.config.MongodConfig
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.process.runtime.Network
import de.flapdoodle.embed.mongo.MongodStarter._
import org.springframework.data.mongodb.core.MongoTemplate
import com.mongodb.Mongo
import org.springframework.scala.context.function.{ContextSupport, FunctionalConfiguration}
import scala.Some
import microon.services.userdirectory.{UserDirectoryService, User}
import microon.ri.boot.spring.scala.SpringScalaBoot

@RunWith(classOf[JUnitRunner])
class DefaultUserDirectoryServiceTest extends FunSuite with BeforeAndAfter with BeforeAndAfterAll {

  // Infrastructure fixtures

  val mongoConfig = new MongodConfig(Version.Main.PRODUCTION, 27017, Network.localhostIsIPv6())
  val mongoDaemon = getDefaultInstance.prepare(mongoConfig).start()
  val mongo = new Mongo

  // Services fixtures

  val boot = SpringScalaBoot[TestConfig].start()
  var service = boot.context[UserDirectoryService]

  override def afterAll(configMap: Map[String, Any]) {
    mongoDaemon.stop()
  }

  before {
    mongo.dropDatabase(DefaultUserDirectoryService.userDirectoryDBName)
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
    new MongoTemplate(new Mongo, DefaultUserDirectoryService.userDirectoryDBName)
  }

  bean()(new DefaultUserDirectoryService(mongoTemplate()))

}