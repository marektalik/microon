package efsp.services.auth.googleoauth

import javax.servlet.http.HttpServletResponse
import efsp.services.auth.AuthRequest

case class GoogleOAuthAuthRequest(httpResponse: HttpServletResponse) extends AuthRequest

