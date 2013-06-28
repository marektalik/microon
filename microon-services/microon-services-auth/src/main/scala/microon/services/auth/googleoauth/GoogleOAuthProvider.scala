package efsp.services.auth.googleoauth

import efsp.services.auth.{AuthProvider, AuthRequest, UserRegistry}
import java.net.URL
import com.google.api.client.googleapis.auth.oauth2.{GoogleCredential, GoogleBrowserClientRequestUrl}
import javax.servlet.http.HttpServletRequest
import com.google.api.services.oauth2.Oauth2
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import scala.collection.JavaConversions._

class GoogleOAuthProvider(userRegistry: UserRegistry, userIdResolver: UserIdResolver, userInfoValidator: UserInfoValidator, cliendId: String, redirectUrl: URL, profiles: Seq[String]) extends AuthProvider {

  def auth(authRequest: AuthRequest) {
    val oauth = authRequest.asInstanceOf[GoogleOAuthAuthRequest]
    val oauthUrl = new GoogleBrowserClientRequestUrl(cliendId, redirectUrl.toString, profiles).setState("/").build
    oauth.httpResponse.sendRedirect(oauthUrl)
  }

  def handleResponse(httpRequest: HttpServletRequest): String = {
    val accessToken = httpRequest.getParameter("access_token")
    val credential = new GoogleCredential().setAccessToken(accessToken)
    val service = new Oauth2.Builder(new NetHttpTransport(), new JacksonFactory(), credential).build
    def userInfo = service.userinfo.get.execute
    val validation = userInfoValidator.validateUserInfo(userInfo)
    val userId = userIdResolver.resolveUserId(userInfo)
    if (validation.isEmpty)
      userRegistry.logIn(userId)
    else
      userRegistry.logFailureMessage(userId, validation.get)
    userId
  }

  def supports(authRequest: AuthRequest): Boolean =
    authRequest.isInstanceOf[GoogleOAuthAuthRequest]

}
