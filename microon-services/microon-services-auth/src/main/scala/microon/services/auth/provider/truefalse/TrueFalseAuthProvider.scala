package microon.services.auth.provider.truefalse

import TrueFalseAuthProvider.failureMessage
import microon.services.auth.impl.{UserRegistry, AuthProvider}
import microon.services.auth.api.java.AuthRequest

class TrueFalseAuthProvider(userRegistry: UserRegistry) extends AuthProvider {

  def authenticator: PartialFunction[AuthRequest, Unit] = {
    case request: TrueFalseAuthRequest => {
      val userId = request.userId
      if (request.expectedResult) {
        userRegistry.logIn(userId)
      } else {
        userRegistry.logFailureMessage(userId, failureMessage(userId))
      }
    }
  }

}

object TrueFalseAuthProvider {

  def failureMessage(userId: String): String =
    "Log in attempt of user %s failed as requested.".format(userId)

}