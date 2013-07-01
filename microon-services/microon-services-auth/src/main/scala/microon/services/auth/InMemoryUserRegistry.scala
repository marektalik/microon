package microon.services.auth

import scala.collection.concurrent.TrieMap
import java.util.Date

class InMemoryUserRegistry extends UserRegistry {

  val loggedUsers = TrieMap.empty[String, Date]
  val lastLoginFailureMessage = TrieMap.empty[String, String]

  def logIn(userId: String) {
    loggedUsers += (userId -> new Date)
  }

  def isLoggedIn(userId: String): Boolean = {
    loggedUsers.get(userId).isDefined
  }

  def logFailureMessage(userId: String, message: String) {
    lastLoginFailureMessage += (userId -> message)
  }

  def lastLogInFailureMessage(userId: String): Option[String] = {
    lastLoginFailureMessage.get(userId)
  }

  def logout(userId: String) {
    loggedUsers -= userId
  }

}