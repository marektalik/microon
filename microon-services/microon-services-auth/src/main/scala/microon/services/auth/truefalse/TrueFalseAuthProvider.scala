package microon.services.auth.truefalse

import microon.services.auth.{UserRegistry, AuthRequest, AuthProvider}
import TrueFalseAuthProvider.failureMessage

class TrueFalseAuthProvider(userRegistry: UserRegistry) extends AuthProvider {

  def auth(authRequest: AuthRequest) {
    val trueFalseAuthRequest = authRequest.asInstanceOf[TrueFalseAuthRequest]
    val userId = trueFalseAuthRequest.userId
    if (trueFalseAuthRequest.expectedResult) {
      userRegistry.logIn(userId)
    } else {
      userRegistry.logFailureMessage(userId, failureMessage(userId))
    }
  }

  def supports(authRequest: AuthRequest): Boolean =
    authRequest.isInstanceOf[TrueFalseAuthRequest]

}

object TrueFalseAuthProvider {

  def failureMessage(userId: String): String =
    "Log in attempt of user %s failed as requested.".format(userId)

}