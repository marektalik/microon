package microon.services.usermanagement.impl.callback.activation

import microon.services.usermanagement.api.scala.User
import microon.services.repository.api.scala.RepositoryService
import microon.services.usermanagement.impl.callback.AfterUserRegistrationCallback

class ActivationRequiredCallback[U <: User, T <: UserActivationToken]
(userActivationTokenFactory: UserActivationTokenFactory[T],
 tokenRepositoryService: RepositoryService[T, java.lang.Long],
 userRepositoryService: RepositoryService[U, java.lang.Long])
  extends AfterUserRegistrationCallback[U] {

  def callback: PartialFunction[U, Unit] = {
    case user: U => {
      val token = userActivationTokenFactory.createToken(user.id, System.nanoTime + "")
      tokenRepositoryService.save(token).get
      user.active(active = false)
      userRepositoryService.save(user).get
    }
  }

}