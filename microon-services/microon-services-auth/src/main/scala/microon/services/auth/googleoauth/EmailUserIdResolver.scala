package microon.services.auth.googleoauth

import com.google.api.services.oauth2.model.Userinfo

class EmailUserIdResolver extends UserIdResolver {
  def resolveUserId(userInfo: Userinfo): String = userInfo.getEmail
}
