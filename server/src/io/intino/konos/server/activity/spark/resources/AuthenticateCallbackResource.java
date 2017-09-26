package io.intino.konos.server.activity.spark.resources;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.services.AuthService;
import io.intino.konos.server.activity.services.AuthService.Authentication;
import io.intino.konos.server.activity.services.auth.Token;
import io.intino.konos.server.activity.services.auth.UserInfo;
import io.intino.konos.server.activity.services.auth.Verifier;
import io.intino.konos.server.activity.services.auth.exceptions.CouldNotObtainAccessToken;
import io.intino.konos.server.activity.services.auth.exceptions.CouldNotObtainInfo;
import io.intino.konos.server.activity.services.push.User;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.spark.actions.AuthenticateCallbackAction;

import java.util.Optional;

public class AuthenticateCallbackResource extends Resource {

    public AuthenticateCallbackResource(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
        super(manager, notifierProvider);
    }

    @Override
    public void execute() throws KonosException {
        super.execute();

        AuthenticateCallbackAction action = new AuthenticateCallbackAction();
        action.session = manager.currentSession();

        try {
            verifyAccessToken();
            listenForLogOut(action);
            action.whenLoggedIn(userOf(userInfo()));

            manager.redirect(manager.baseUrl() + userHomePath());
        } catch (CouldNotObtainAccessToken error) {
            manager.write(error);
        }
    }

    private Token verifyAccessToken() throws CouldNotObtainAccessToken {
        Optional<Authentication> authentication = authenticationOf(manager.fromQuery("authId", String.class));

        if (!authentication.isPresent())
            return null;

        String oauthVerifier = manager.fromQuery("oauth_verifier", String.class);
        return authentication.get().accessToken(Verifier.build(oauthVerifier));
    }

    private void listenForLogOut(AuthenticateCallbackAction action) {
        try {
            manager.authService().addPushListener(accessToken(), pushListener(action));
        } catch (CouldNotObtainInfo error) {
            error.printStackTrace();
        }
    }

    private Token accessToken() {
        return authentication().orElse(null).accessToken();
    }

    private AuthService.FederationNotificationListener pushListener(AuthenticateCallbackAction action) {
        return new AuthService.FederationNotificationListener() {
            @Override
            public void userLoggedOut(UserInfo userInfo) {
                action.whenLoggedOut(userOf(userInfo));
            }

            @Override
            public void userAdded(UserInfo userInfo) {
            }
        };
    }

    private UserInfo userInfo() {
        try {
            return manager.authService().me(accessToken());
        } catch (CouldNotObtainInfo error) {
            error.printStackTrace();
            throw new RuntimeException(error);
        }
    }

    private User userOf(UserInfo info) {
        if (info == null) return null;

        User user = new User();
        user.username(info.username());
        user.fullName(info.fullName());
        user.email(info.email());
        user.language(info.language());
        user.photo(info.photo());

        return user;
    }

}
