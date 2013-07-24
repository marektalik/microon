package microon.services.auth.provider.truefalse

import microon.services.auth.api.scala.AuthRequest

case class TrueFalseAuthRequest(userId: String, expectedResult: Boolean) extends AuthRequest