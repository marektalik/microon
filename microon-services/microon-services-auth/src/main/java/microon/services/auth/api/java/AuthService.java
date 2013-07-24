package microon.services.auth.api.java;

import microon.spi.scala.activeobject.Void;

import java.util.concurrent.Future;

public interface AuthService {

    Future<Void> auth(AuthRequest authRequest);

    Future<Boolean> isLoggedIn(String userId);

    Future<String> lastLogInFailureMessage(String userId);

    Future<Void> logout(String userId);

}