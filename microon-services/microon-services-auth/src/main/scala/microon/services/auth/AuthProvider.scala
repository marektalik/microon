package microon.services.auth

trait AuthProvider {

  def auth(authRequest: AuthRequest)

  def supports(authRequest: AuthRequest): Boolean

}