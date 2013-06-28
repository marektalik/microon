package microon.services.auth.truefalse

import microon.services.auth.AuthRequest

case class TrueFalseAuthRequest(userId: String, expectedResult: Boolean) extends AuthRequest