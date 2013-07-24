package microon.services.auth.impl

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import microon.services.auth.provider.truefalse.{TrueFalseAuthRequest, TrueFalseAuthProvider}
import TrueFalseAuthProvider.failureMessage
import microon.ri.boot.spring.scala.SpringScalaBoot
import org.springframework.scala.context.function.FunctionalConfiguration
import microon.services.auth.api.scala.AuthService

@RunWith(classOf[JUnitRunner])
class DefaultAuthServiceTest extends FunSuite with BeforeAndAfter {

  // Data fixtures

  val userId = "userId"

  // Service fixtures

  var service: AuthService = _

  before {
    val boot = SpringScalaBoot[TestConfig].start()
    service = boot.context[AuthService]
  }

  // Tests

  test("Should log in user.") {
    expectResult(true) {
      service.auth(TrueFalseAuthRequest(userId, expectedResult = true)).get
      service.isLoggedIn(userId).get
    }
  }

  test("Should not log in user.") {
    service.auth(TrueFalseAuthRequest(userId, expectedResult = false)).get
    assert(service.isLoggedIn(userId).get === false)
    assert(service.lastLogInFailureMessage(userId).get === Some(failureMessage(userId)))
  }

  test("Should log out user.") {
    expectResult(false) {
      service.auth(TrueFalseAuthRequest(userId, expectedResult = true)).get
      service.logout(userId).get
      service.isLoggedIn(userId).get
    }
  }

}

class TestConfig extends FunctionalConfiguration {

  bean() {
    val userRegistry = new InMemoryUserRegistry
    val provider = new TrueFalseAuthProvider(userRegistry)
    new DefaultAuthService(userRegistry, Seq(provider))
  }

}