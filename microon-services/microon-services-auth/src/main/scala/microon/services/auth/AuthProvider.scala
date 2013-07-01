package microon.services.auth

trait AuthProvider {

  def authenticator: PartialFunction[AuthRequest, Unit]

}