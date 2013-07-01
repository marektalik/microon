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
import org.springframework.scala.context.function.{ContextSupport, FunctionalConfiguration, FunctionalConfigApplicationContext}
import java.util.concurrent.Executors._
import scala.Some
import microon.services.userdirectory.User
import microon.ri.activeobject.ExecutorServiceActiveObjectDispatcher

@RunWith(classOf[JUnitRunner])
class DefaultUserDirectoryServiceTest extends FunSuite with BeforeAndAfter with BeforeAndAfterAll {

  val mongoConfig = new MongodConfig(Version.Main.PRODUCTION, 27017, Network.localhostIsIPv6())
  val mongoDaemon = getDefaultInstance.prepare(mongoConfig).start()
  val mongo = new Mongo

  var userDirectory: DefaultUserDirectoryService = _

  override def afterAll(configMap: Map[String, Any]) {
    mongoDaemon.stop()
  }

  before {
    mongo.dropDatabase(DefaultUserDirectoryService.userDirectoryDBName)
    val context = FunctionalConfigApplicationContext[TestConfig]
    userDirectory = context.bean[DefaultUserDirectoryService].get
  }

  test("Should not find non-existing user.") {
    expectResult(false) {
      val randomUserId = "123456789012345678901234"
      userDirectory.userExists(randomUserId).get
    }
  }

  test("Should find existing user.") {
    val userId = userDirectory.createUser().get
    expectResult(true) {
      userDirectory.userExists(userId).get
    }
  }

  // Properties access

  test("Should load user properties.") {
    val userId = userDirectory.createUser(Map("foo" -> "bar")).get
    expectResult(Map("foo" -> "bar")) {
      userDirectory.loadUserProperties(userId).get
    }
  }

  test("Should load user property.") {
    val userId = userDirectory.createUser(Map("foo" -> "bar")).get
    expectResult(Some("bar")) {
      userDirectory.loadUserProperty(userId, "foo").get
    }
  }

  test("Should return None for non-existing user property.") {
    val userId = userDirectory.createUser().get
    expectResult(None) {
      userDirectory.loadUserProperty(userId, "randomProperty").get
    }
  }

  test("Should load user id by property.") {
    val userId = userDirectory.createUser(Map("foo" -> "bar")).get
    expectResult(Some(userId)) {
      userDirectory.loadUserIdByProperty("foo", "bar").get
    }
  }

  test("Should list users properties.") {
    val properties = Map("foo" -> "bar", "baz" -> "qux")
    val user1Id = userDirectory.createUser(properties).get
    val user2Id = userDirectory.createUser(properties).get
    expectResult(Seq(User(user1Id, Map("foo" -> "bar")), User(user2Id, Map("foo" -> "bar")))) {
      userDirectory.listUsersProperties(Seq("foo")).get
    }
  }

  test("Should update user properties.") {
    val userId = userDirectory.createUser(Map("foo" -> "bar")).get
    expectResult(Map("baz" -> "qux", "foo" -> "bar")) {
      userDirectory.updateUserProperties(userId, Map("baz" -> "qux")).get
      userDirectory.loadUserProperties(userId).get
    }
  }

}

class TestConfig extends FunctionalConfiguration with ContextSupport {
  enableAnnotationConfig()

  val executor = bean()(newCachedThreadPool)
  bean()(new ExecutorServiceActiveObjectDispatcher(executor()))

  val mongoTemplate = bean() {
    new MongoTemplate(new Mongo, DefaultUserDirectoryService.userDirectoryDBName)
  }
  bean()(new DefaultUserDirectoryService(mongoTemplate()))
}