package microon.services.auth.impl

import microon.services.auth._
import java.util.concurrent.Future
import microon.spi.scala.activeobject.{ActiveObject, Void}

class AuthServiceImpl(userRegistry: UserRegistry, authProviders: Seq[AuthProvider]) extends AuthService with ActiveObject {

  var authenticators = PartialFunction.empty[AuthRequest, Unit]
  authProviders.foreach(provider => authenticators = authenticators orElse provider.authenticator)

  def auth(authRequest: AuthRequest): Future[Void] = void {
    authenticators(authRequest)
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