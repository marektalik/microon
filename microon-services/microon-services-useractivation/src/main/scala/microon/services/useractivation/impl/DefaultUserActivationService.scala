package microon.services.useractivation.impl

import microon.spi.scala.activeobject.{Void, ActiveObject}
import java.util.concurrent.Future
import microon.services.repository.api.scala.RepositoryService
import java.lang.Long
import microon.services.usermanagement.impl.callback.{BeforeUserRegistrationCallback, AfterUserRegistrationCallback}
import microon.services.useractivation.api.scala.{UserActivationService, UserActivationToken, ActivableUser}

class DefaultUserActivationService[U <: ActivableUser, T <: UserActivationToken]
(repositoryService: RepositoryService[U, Long],
 beforeUserRegistrationCallbacks: Seq[BeforeUserRegistrationCallback[U]] = Seq.empty,
 afterUserRegistrationCallbacks: Seq[AfterUserRegistrationCallback[U]] = Seq.empty,
 tokenRepositoryService: RepositoryService[T, java.lang.Long])
  extends UserActivationService with ActiveObject {

  def activateUser(userId: scala.Long, code: String): Future[Void] = void {
    val token = tokenRepositoryService.findOneByQuery(code).get
    if (token.userId == userId) {
      val user = repositoryService.findOne(userId).get.get
      user.active(true)
      repositoryService.save(user)
    }
  }

}