package microon.services.userdirectory

import java.util.concurrent.Future

trait UserDirectory {

  // User existence

  def userExists(id: String): Boolean

  def createUser(initialProperties: Map[String, String] = Map.empty): String

  // Properties access

  def loadUserProperties(id: String): Map[String, String]

  def loadUserProperty(id: String, property: String): Option[String]

  def loadUserIdByProperty(propertyName: String, propertyValue: String): Future[Option[String]]

  def listUsersProperties(properties: Seq[String]): Seq[User]

  // Properties modification

  def updateUserProperties(id: String, properties: Map[String, String])

}

case class User(id: String, properties: Map[String, String])