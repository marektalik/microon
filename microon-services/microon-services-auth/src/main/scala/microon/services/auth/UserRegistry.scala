package efsp.services.auth

import scala.Predef.String

trait UserRegistry {

  def logIn(userId: String)

  def isLoggedIn(userId: String): Boolean

  def logFailureMessage(userId: String, message: String)

  def lastLogInFailureMessage(userId: String): Option[String]

}