package microon.services.auth

class AuthService(authProviders: Seq[AuthProvider]) {

  def auth(authRequest: AuthRequest) {
    authProviders.filter(_.supports(authRequest)).last.auth(authRequest)
  }

}