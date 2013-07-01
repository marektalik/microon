package microon.services.auth

import java.util.Date
import com.google.common.cache.CacheBuilder
import java.util.concurrent.TimeUnit

class InMemoryUserRegistry extends UserRegistry {

  val loggedUsers = CacheBuilder.newBuilder.expireAfterAccess(1, TimeUnit.HOURS).build[String, Date]
  val lastLoginFailureMessage = CacheBuilder.newBuilder.expireAfterAccess(1, TimeUnit.HOURS).build[String, String]

  def logIn(userId: String) {
    loggedUsers.put(userId, new Date)
  }

  def isLoggedIn(userId: String): Boolean =
    loggedUsers.getIfPresent(userId) != null

  def logFailureMessage(userId: String, message: String) {
    lastLoginFailureMessage.put(userId, message)
  }

  def lastLogInFailureMessage(userId: String): Option[String] =
    Option(lastLoginFailureMessage.getIfPresent(userId))

  def logout(userId: String) {
    loggedUsers.invalidate(userId)
  }

}