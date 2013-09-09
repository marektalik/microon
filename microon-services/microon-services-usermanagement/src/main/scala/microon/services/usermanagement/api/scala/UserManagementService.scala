package microon.services.usermanagement.api.scala

import java.util.concurrent.Future

trait UserManagementService[U <: User] {

  def registerUser(user: U): Future[java.lang.Long]

}