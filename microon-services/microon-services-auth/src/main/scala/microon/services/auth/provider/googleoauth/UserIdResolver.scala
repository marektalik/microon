package microon.services.auth.provider.googleoauth

import com.google.api.services.oauth2.model.Userinfo

trait UserIdResolver {

  def resolveUserId(userInfo: Userinfo): String

}
