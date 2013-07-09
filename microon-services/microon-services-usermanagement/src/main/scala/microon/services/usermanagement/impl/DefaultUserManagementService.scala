package microon.services.usermanagement.impl

import microon.spi.scala.activeobject.{Void, ActiveObject}
import java.util.concurrent.Future
import microon.services.userdirectory.UserDirectoryService
import microon.services.usermanagement.{User, UserManagementService}
import javax.inject.Inject

class DefaultUserManagementService(@Inject userDirectoryService: UserDirectoryService, userProperties: Seq[String])
  extends UserManagementService with ActiveObject {

  def registerUser(username: String): Future[String] = dispatch {
    userDirectoryService.createUser(Map("username" -> username)).get
  }

  def userExists(username: String): Future[Boolean] = dispatch {
    userDirectoryService.loadUserIdByProperty("username", username).get.isDefined
  }

  def loadUser(id: String): Future[User] = dispatch {
    User(id, userDirectoryService.loadUserProperties(id).get)
  }

  def listUsers: Future[Seq[User]] = dispatch {
    userDirectoryService.listUsersProperties(userProperties).get.map {
      user => User(user.id, user.properties.keys.foldLeft(Map.empty[String, String])((props, key) => props + (key -> user.properties(key))))
    }
  }

  def updateUser(user: User): Future[Void] = void {
    userDirectoryService.updateUserProperties(user.id, user.properties)
  }

}