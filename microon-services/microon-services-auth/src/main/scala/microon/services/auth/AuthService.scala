package microon.services.auth

import efsp.services.auth.{AuthProvider, AuthRequest}

class AuthService(authProviders: Seq[AuthProvider]) {

  def auth(authRequest: AuthRequest) {
    authProviders.filter(_.supports(authRequest)).last.auth(authRequest)
  }

}