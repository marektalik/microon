package microon.services.auth.googleoauth

import com.google.api.services.oauth2.model.Userinfo

trait UserInfoValidator {

  def validateUserInfo(userInfo: Userinfo): Option[String]

}
