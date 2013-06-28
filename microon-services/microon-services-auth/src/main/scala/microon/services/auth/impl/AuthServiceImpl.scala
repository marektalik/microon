package microon.services.auth.impl

import microon.services.auth._

class AuthServiceImpl(userRegistry: UserRegistry, authProviders: Seq[AuthProvider]) extends AuthService {

  def auth(authRequest: AuthRequest) {
    authProviders.filter(_.supports(authRequest)).last.auth(authRequest)
  }

  def isLoggedIn(userId: String): Boolean =
    userRegistry.isLoggedIn(userId)

  def lastLogInFailureMessage(userId: String): Option[String] =
    userRegistry.lastLogInFailureMessage(userId)

}