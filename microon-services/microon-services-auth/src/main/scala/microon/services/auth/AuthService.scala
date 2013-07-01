package microon.services.auth

import java.util.concurrent.Future
import microon.spi.scala.activeobject.Void

trait AuthService {

  def auth(authRequest: AuthRequest): Future[Void]

  def isLoggedIn(userId: String): Future[Boolean]

  def lastLogInFailureMessage(userId: String): Future[Option[String]]

  def logout(userId: String): Future[Void]

}