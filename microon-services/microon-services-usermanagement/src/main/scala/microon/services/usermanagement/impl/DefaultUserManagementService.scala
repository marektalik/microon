package microon.services.usermanagement.impl

import microon.spi.scala.activeobject.ActiveObject
import java.util.concurrent.Future
import javax.inject.Inject
import microon.services.repository.api.scala.RepositoryService
import microon.services.usermanagement.api.scala.{User, UserManagementService}
import java.lang.Long

class DefaultUserManagementService[U <: User](@Inject repositoryService: RepositoryService[U, Long])
  extends UserManagementService[U] with ActiveObject {

  def registerUser(user: U): Future[Long] = dispatch {
    repositoryService.save(user).get.id
  }

}