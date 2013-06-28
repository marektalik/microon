package microon.services.auth.googleoauth

import javax.servlet.http.HttpServletResponse
import microon.services.auth.AuthRequest

case class GoogleOAuthAuthRequest(httpResponse: HttpServletResponse) extends AuthRequest

