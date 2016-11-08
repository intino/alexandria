package io.intino.pandora.server.activity.services;

import io.intino.pandora.server.activity.services.auth.*;
import io.intino.pandora.server.activity.services.auth.exceptions.*;

import java.net.URL;

public interface AuthService {

    Authenticating authenticate(URL federation);
    boolean valid(URL federation, Token accessToken);
    FederationInfo info(URL federation, Token accessToken) throws CouldNotObtainInfo;
    UserInfo me(URL federation, Token accessToken) throws CouldNotObtainInfo;
    void logout(URL federation, Token accessToken) throws CouldNotLogout;

    void addPushListener(URL federation, Token accessToken, FederationNotificationListener listener) throws CouldNotObtainInfo;

    interface Authenticating {
        Authentication with(Space space);

        interface Authentication {
            Token requestToken() throws CouldNotObtainRequestToken;
            URL authenticationUrl(Token requestToken) throws CouldNotObtainAuthorizationUrl;
            Token accessToken();
            Token accessToken(Verifier verifier) throws CouldNotObtainAccessToken;
            void invalidate() throws CouldNotInvalidateAccessToken;
        }
    }

    interface FederationNotificationListener {
        void userLoggedOut(UserInfo user);
        void userAdded(UserInfo user);
    }

}
