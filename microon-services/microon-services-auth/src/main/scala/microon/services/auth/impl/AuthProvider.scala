package microon.services.auth.impl

import microon.services.auth.api.java.AuthRequest

/**
 * Provides handler that could be used against the [[AuthRequest]] instance in
 * order to validate the user credentials.
 */
trait AuthProvider {

  /**
   * Return authentication handler.
   *
   * @return Partial function capable of handling the [[AuthRequest]] and invoking procedure
   *         of verification of the user credentials.
   */
  def authenticator: PartialFunction[AuthRequest, Unit]

}