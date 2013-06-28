package microon.services.auth

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfter, FunSuite}
import microon.services.auth.truefalse.{TrueFalseAuthRequest, TrueFalseAuthProvider}
import microon.services.auth.impl.AuthServiceImpl
import TrueFalseAuthProvider.failureMessage

@RunWith(classOf[JUnitRunner])
class AuthServiceImplTest extends FunSuite with BeforeAndAfter {

  // Data fixtures

  val userId = "userId"

  // Collaborators fixture

  var service: AuthService = _

  before {
    val userRegistry = new InMemoryUserRegistry
    val provider = new TrueFalseAuthProvider(userRegistry)
    service = new AuthServiceImpl(userRegistry, Seq(provider))
  }

  // Tests

  test("Should log in user.") {
    expectResult(true) {
      service.auth(TrueFalseAuthRequest(userId, expectedResult = true))
      service.isLoggedIn(userId)
    }
  }

  test("Should not log in user.") {
    service.auth(TrueFalseAuthRequest(userId, expectedResult = false))
    assert(service.isLoggedIn(userId) === false)
    assert(service.lastLogInFailureMessage(userId) === Some(failureMessage(userId)))
  }

}
