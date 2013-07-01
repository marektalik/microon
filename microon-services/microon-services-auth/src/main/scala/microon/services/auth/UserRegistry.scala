package microon.services.auth

trait UserRegistry {

  def logIn(userId: String)

  def isLoggedIn(userId: String): Boolean

  def logFailureMessage(userId: String, message: String)

  def lastLogInFailureMessage(userId: String): Option[String]

  def logout(userId: String)

}