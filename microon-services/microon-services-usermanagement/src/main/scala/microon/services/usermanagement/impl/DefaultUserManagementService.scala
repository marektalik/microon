package microon.services.usermanagement.impl

import microon.spi.scala.activeobject.ActiveObject
import java.util.concurrent.Future
import microon.services.repository.api.scala.RepositoryService
import microon.services.usermanagement.api.scala.{User, UserManagementService}
import java.lang.Long
import microon.services.usermanagement.impl.callback.{BeforeUserRegistrationCallback, AfterUserRegistrationCallback}

class DefaultUserManagementService[U <: User]
(repositoryService: RepositoryService[U, Long],
 beforeUserRegistrationCallbacks: Seq[BeforeUserRegistrationCallback[U]] = Seq.empty,
 afterUserRegistrationCallbacks: Seq[AfterUserRegistrationCallback[U]] = Seq.empty)
  extends UserManagementService[U] with ActiveObject {

  def registerUser(user: U): Future[Long] = dispatch {
    beforeUserRegistrationCallbacks.foreach(_.callback(user))
    val savedUser = repositoryService.save(user).get
    afterUserRegistrationCallbacks.foreach(_.callback(savedUser))
    savedUser.id
  }

}