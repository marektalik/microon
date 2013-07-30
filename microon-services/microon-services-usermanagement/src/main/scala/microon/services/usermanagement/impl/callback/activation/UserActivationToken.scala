package microon.services.usermanagement.impl.callback.activation

trait UserActivationToken {

  def userId: Long

  def activationToken: String

}
