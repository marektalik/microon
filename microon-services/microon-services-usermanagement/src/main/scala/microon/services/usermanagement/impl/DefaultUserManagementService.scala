package microon.services.usermanagement.impl

import microon.spi.scala.activeobject.{Void, ActiveObject}
import java.util.concurrent.Future
import microon.services.repository.api.scala.RepositoryService
import microon.services.usermanagement.api.scala.{User, UserManagementService}
import java.lang.Long
import microon.services.usermanagement.impl.callback.{BeforeUserRegistrationCallback, AfterUserRegistrationCallback}
import microon.services.usermanagement.impl.callback.activation.UserActivationToken

class DefaultUserManagementService[U <: User, T <: UserActivationToken]
(repositoryService: RepositoryService[U, Long],
 beforeUserRegistrationCallbacks: Seq[BeforeUserRegistrationCallback[U]] = Seq.empty,
 afterUserRegistrationCallbacks: Seq[AfterUserRegistrationCallback[U]] = Seq.empty,
 tokenRepositoryService: RepositoryService[T, java.lang.Long])
  extends UserManagementService[U] with ActiveObject {

  def registerUser(user: U): Future[Long] = dispatch {
    beforeUserRegistrationCallbacks.foreach(_.callback(user))
    val savedUser = repositoryService.save(user).get
    afterUserRegistrationCallbacks.foreach(_.callback(savedUser))
    savedUser.id
  }

  def activateUser(userId: scala.Long, code: String): Future[Void] = void {
    val token = tokenRepositoryService.findOneByQuery(code).get
    if (token.userId == userId) {
      val user = repositoryService.findOne(userId).get
      user.active(true)
      repositoryService.save(user)
    }
  }

}