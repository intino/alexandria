package io.intino.alexandria.foundation.activity.services;

import io.intino.alexandria.foundation.activity.services.auth.*;
import io.intino.alexandria.foundation.activity.services.auth.exceptions.*;

import java.net.URL;

public interface AuthService {

    URL url();
    Space space();
    Authentication authenticate() throws SpaceAuthCallbackUrlIsNull;
    boolean valid(Token accessToken);
    FederationInfo info(Token accessToken) throws CouldNotObtainInfo;
    UserInfo me(Token accessToken) throws CouldNotObtainInfo;
    void logout(Token accessToken) throws CouldNotLogout;

    void addPushListener(Token accessToken, FederationNotificationListener listener) throws CouldNotObtainInfo;

    interface Authentication {
        Token requestToken() throws CouldNotObtainRequestToken;
        URL authenticationUrl(Token requestToken) throws CouldNotObtainAuthorizationUrl;
        Token accessToken();
        Token accessToken(Verifier verifier) throws CouldNotObtainAccessToken;
        void invalidate() throws CouldNotInvalidateAccessToken;
    }

    interface FederationNotificationListener {
        void userLoggedOut(UserInfo user);
        void userAdded(UserInfo user);
    }

}
