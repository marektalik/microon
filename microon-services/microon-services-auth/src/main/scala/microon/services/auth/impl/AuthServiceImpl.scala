package microon.services.auth.impl

import microon.services.auth._
import java.util.concurrent.Future
import microon.spi.scala.activeobject.{ActiveObject, Void}

class AuthServiceImpl(userRegistry: UserRegistry, authProviders: Seq[AuthProvider]) extends AuthService with ActiveObject {

  def auth(authRequest: AuthRequest): Future[Void] = void {
    authProviders.filter(_.supports(authRequest)).last.auth(authRequest)
  }

  def isLoggedIn(userId: String): Future[Boolean] = dispatch {
    userRegistry.isLoggedIn(userId)
  }

  def lastLogInFailureMessage(userId: String): Future[Option[String]] = dispatch {
    userRegistry.lastLogInFailureMessage(userId)
  }

  def logout(userId: String): Future[Void] = void {
    userRegistry.logout(userId)
  }

}