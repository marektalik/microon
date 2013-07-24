package microon.services.auth.impl

import microon.services.auth.api.scala.{AuthRequest, AuthService}
import microon.services.auth.api.java.{AuthService => JavaAuthService, AuthRequest => JavaAuthRequest}
import java.util.concurrent.Future
import microon.spi.scala.activeobject.Void
import java.lang.Boolean
import com.google.common.util.concurrent.Futures.immediateFuture

class AuthServiceJavaWrapper(authService: AuthService) extends JavaAuthService {

  def auth(authRequest: JavaAuthRequest): Future[Void] =
    authService.auth(authRequest.asInstanceOf[AuthRequest])

  def isLoggedIn(userId: String): Future[Boolean] =
    immediateFuture(authService.isLoggedIn(userId).get)

  def lastLogInFailureMessage(userId: String): Future[String] =
    immediateFuture(authService.lastLogInFailureMessage(userId).get.getOrElse(null))

  def logout(userId: String): Future[Void] =
    authService.logout(userId)

}