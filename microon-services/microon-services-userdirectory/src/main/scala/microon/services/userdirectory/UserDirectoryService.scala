package microon.services.userdirectory

import java.util.concurrent.Future
import microon.spi.scala.activeobject.Void

trait UserDirectoryService {

  // User existence

  def userExists(userId: String): Future[Boolean]

  def createUser(initialProperties: Map[String, String] = Map.empty): Future[String]

  // Properties access

  def loadUserProperties(userId: String): Future[Map[String, String]]

  def loadUserProperty(userId: String, property: String): Future[Option[String]]

  def loadUserIdByProperty(propertyName: String, propertyValue: String): Future[Option[String]]

  def listUsersProperties(properties: Seq[String]): Future[Seq[User]]

  // Properties modification

  def updateUserProperties(userId: String, properties: Map[String, String]): Future[Void]

}

case class User(id: String, properties: Map[String, String])