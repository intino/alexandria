package io.intino.alexandria.ui.services;

import io.intino.alexandria.ui.services.auth.*;
import io.intino.alexandria.ui.services.auth.exceptions.*;

import java.net.URL;

public interface AuthService {

    URL url();
    Space space();
    Authentication authenticate() throws SpaceAuthCallbackUrlIsNull;
    boolean valid(Token accessToken);
    FederationInfo info(Token accessToken) throws CouldNotObtainInfo;
    UserInfo me(Token accessToken) throws CouldNotObtainInfo;
    void logout(Token accessToken) throws CouldNotLogout;
    String logoutUrl();

    void addPushListener(Token accessToken, FederationNotificationListener listener) throws CouldNotObtainInfo;

    interface Authentication {
        enum Version { OAuth1, OAuth2 }

        Token requestToken() throws CouldNotObtainRequestToken;
        URL authenticationUrl(Token requestToken) throws CouldNotObtainAuthorizationUrl;
        Token accessToken();
        Token accessToken(Verifier verifier) throws CouldNotObtainAccessToken;
        void invalidate() throws CouldNotInvalidateAccessToken;
        Version version();
    }

    interface FederationNotificationListener {
        void userLoggedOut(UserInfo user);
        void userAdded(UserInfo user);
    }

}
