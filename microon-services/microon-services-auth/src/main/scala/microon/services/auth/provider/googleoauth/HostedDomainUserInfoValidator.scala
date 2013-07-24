package microon.services.auth.provider.googleoauth

import com.google.api.services.oauth2.model.Userinfo

class HostedDomainUserInfoValidator(hostedDomain: String) extends UserInfoValidator {
  def validateUserInfo(userInfo: Userinfo): Option[String] = {
    if (userInfo.getHd != hostedDomain) {
      Some("Invalid domain: " + userInfo.getHd)
    } else {
      None
    }
  }
}