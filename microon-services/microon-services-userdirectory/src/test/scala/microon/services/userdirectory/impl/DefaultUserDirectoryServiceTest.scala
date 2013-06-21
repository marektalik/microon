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
import microon.spi.scala.activeobject.ExecutorServiceActiveObjectDispatcher
import microon.services.userdirectory.User

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
      userDirectory.userExists(randomUserId)
    }
  }

  test("Should find existing user.") {
    val userId = userDirectory.createUser()
    expectResult(true) {
      userDirectory.userExists(userId)
    }
  }

  // Properties access

  test("Should load user properties.") {
    val userId = userDirectory.createUser(Map("foo" -> "bar"))
    expectResult(Map("foo" -> "bar")) {
      userDirectory.loadUserProperties(userId)
    }
  }

  test("Should load user property.") {
    val userId = userDirectory.createUser(Map("foo" -> "bar"))
    expectResult(Some("bar")) {
      userDirectory.loadUserProperty(userId, "foo")
    }
  }

  test("Should return None for non-existing user property.") {
    val userId = userDirectory.createUser()
    expectResult(None) {
      userDirectory.loadUserProperty(userId, "randomProperty")
    }
  }

  test("Should load user id by property.") {
    val userId = userDirectory.createUser(Map("foo" -> "bar"))
    expectResult(Some(userId)) {
      userDirectory.loadUserIdByProperty("foo", "bar").get
    }
  }

  test("Should list users properties.") {
    val properties = Map("foo" -> "bar", "baz" -> "qux")
    val user1Id = userDirectory.createUser(properties)
    val user2Id = userDirectory.createUser(properties)
    expectResult(Seq(User(user1Id, Map("foo" -> "bar")), User(user2Id, Map("foo" -> "bar")))) {
      userDirectory.listUsersProperties(Seq("foo"))
    }
  }

  test("Should update user properties.") {
    val userId = userDirectory.createUser(Map("foo" -> "bar"))
    expectResult(Map("baz" -> "qux", "foo" -> "bar")) {
      userDirectory.updateUserProperties(userId, Map("baz" -> "qux"))
      userDirectory.loadUserProperties(userId)
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