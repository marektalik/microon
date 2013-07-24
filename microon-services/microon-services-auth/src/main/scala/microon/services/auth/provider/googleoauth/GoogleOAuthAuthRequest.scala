package microon.services.auth.provider.googleoauth

import javax.servlet.http.HttpServletResponse
import microon.services.auth.api.scala.AuthRequest

case class GoogleOAuthAuthRequest(httpResponse: HttpServletResponse) extends AuthRequest