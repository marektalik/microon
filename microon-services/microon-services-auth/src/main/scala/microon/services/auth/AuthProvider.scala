package microon.services.auth

/**
 * Provides handler that could be used against the [[microon.services.auth.AuthRequest]] instance in
 * order to validate the user credentials.
 */
trait AuthProvider {

  /**
   * Return authentication handler.
   *
   * @return Partial function capable of handling the [[microon.services.auth.AuthRequest]] and invoking procedure
   *         of verification of the user credentials.
   */
  def authenticator: PartialFunction[AuthRequest, Unit]

}