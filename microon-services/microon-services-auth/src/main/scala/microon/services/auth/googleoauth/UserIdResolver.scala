package microon.services.auth.googleoauth

import com.google.api.services.oauth2.model.Userinfo

trait UserIdResolver {

  def resolveUserId(userInfo: Userinfo): String

}
