package microon.services.usermanagement.impl.callback.activation

trait UserActivationTokenFactory[T] {

  def createToken(userId: Long, token: String): T

}
