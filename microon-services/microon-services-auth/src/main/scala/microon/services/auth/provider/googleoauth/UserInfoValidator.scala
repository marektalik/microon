package microon.services.auth.provider.googleoauth

import com.google.api.services.oauth2.model.Userinfo

trait UserInfoValidator {

  def validateUserInfo(userInfo: Userinfo): Option[String]

}
