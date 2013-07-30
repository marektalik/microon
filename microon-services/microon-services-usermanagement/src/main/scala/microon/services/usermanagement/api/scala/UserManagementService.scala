package microon.services.usermanagement.api.scala

import java.util.concurrent.Future
import microon.spi.scala.activeobject.Void

trait UserManagementService[U <: User] {

  def registerUser(user: U): Future[java.lang.Long]

  def activateUser(userId: Long, code: String): Future[Void]

}