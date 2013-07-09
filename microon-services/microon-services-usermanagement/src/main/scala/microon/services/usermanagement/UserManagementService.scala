package microon.services.usermanagement

import java.util.concurrent.Future
import microon.spi.scala.activeobject.Void

trait UserManagementService {

  def registerUser(username: String): Future[String]

  def userExists(username: String): Future[Boolean]

  def loadUser(id: String): Future[User]

  def listUsers: Future[Seq[User]]

  def updateUser(user: User): Future[Void]

}



