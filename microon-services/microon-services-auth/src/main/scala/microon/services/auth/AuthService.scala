package microon.services.auth

trait AuthService {

  def auth(authRequest: AuthRequest)

  def isLoggedIn(userId: String): Boolean

  def lastLogInFailureMessage(userId: String): Option[String]

}