package microon.services.usermanagement.impl

import org.scalatest.{BeforeAndAfter, FunSuite}
import microon.ri.boot.spring.scala.SpringScalaBoot
import org.springframework.scala.context.function.{ContextSupport, FunctionalConfiguration}
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import microon.services.usermanagement.api.scala.{User, UserManagementService}
import org.mockito.Mockito._
import org.mockito.BDDMockito._
import microon.services.repository.api.scala.RepositoryService
import org.scalatest.mock.MockitoSugar
import com.google.common.util.concurrent.Futures.immediateFuture
import microon.services.usermanagement.impl.callback.BeforeUserRegistrationCallback

@RunWith(classOf[JUnitRunner])
class DefaultUserManagementServiceTest extends FunSuite with BeforeAndAfter {

  // Services fixtures

  val boot = SpringScalaBoot[TestConfig].start()
  val repository = boot.context[RepositoryService[TestUser, java.lang.Long]]
  var service = boot.context[UserManagementService[TestUser]]

  before {
    reset(repository)
  }

  // Data fixtures

  val user = TestUser()

  // Tests

  test("Should register user.") {
    // Given
    given(repository.save(user)).willReturn(immediateFuture(TestUser(1)))

    // When
    service.registerUser(user).get

    // Then
    verify(repository).save(user)
  }

  test("Should return id of registered user.") {
    // Given
    val mockedId = -1
    given(repository.save(user)).willReturn(immediateFuture(TestUser(mockedId)))

    // When
    val id = service.registerUser(user).get

    // Then
    assert(mockedId === id)
  }

  test("Should execute callback.") {
    // Given
    given(repository.save(user)).willReturn(immediateFuture(TestUser(1)))

    // When
    service.registerUser(user).get

    // Then
    assert(user === TestUserHolder.lastUser)
  }

}

class TestConfig extends FunctionalConfiguration with ContextSupport with MockitoSugar {
  enableAnnotationConfig()

  val repository = bean() {
    mock[RepositoryService[TestUser, java.lang.Long]]
  }

  val beforeCallbacks = bean() {
    Seq(
      BeforeUserRegistrationCallback[TestUser] {
        case user: TestUser => TestUserHolder.lastUser = user
      }
    )
  }

  bean()(new DefaultUserManagementService(repository(), beforeCallbacks(), Seq.empty))

}

case class TestUser(var id: java.lang.Long = null, var active: Boolean = true) extends User

object TestUserHolder {
  var lastUser: TestUser = _
}