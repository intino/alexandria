package io.intino.konos.alexandria.ui.spark.resources;

import io.intino.konos.alexandria.exceptions.AlexandriaException;
import io.intino.konos.alexandria.ui.displays.AlexandriaDisplayNotifierProvider;
import io.intino.konos.alexandria.ui.services.AuthService;
import io.intino.konos.alexandria.ui.services.AuthService.Authentication;
import io.intino.konos.alexandria.ui.services.auth.Token;
import io.intino.konos.alexandria.ui.services.auth.UserInfo;
import io.intino.konos.alexandria.ui.services.auth.Verifier;
import io.intino.konos.alexandria.ui.services.auth.exceptions.CouldNotObtainAccessToken;
import io.intino.konos.alexandria.ui.services.auth.exceptions.CouldNotObtainInfo;
import io.intino.konos.alexandria.ui.services.push.User;
import io.intino.konos.alexandria.ui.spark.UISparkManager;
import io.intino.konos.alexandria.ui.spark.actions.AuthenticateCallbackAction;

import java.util.Optional;

public class AuthenticateCallbackResource extends Resource {

    public AuthenticateCallbackResource(UISparkManager manager, AlexandriaDisplayNotifierProvider notifierProvider) {
        super(manager, notifierProvider);
    }

    @Override
    public void execute() throws AlexandriaException {
        super.execute();

        AuthenticateCallbackAction action = new AuthenticateCallbackAction();
        action.session = manager.currentSession();

        try {
            verifyAccessToken();
            listenForLogOut(action);
            action.whenLoggedIn(userOf(userInfo()));

            manager.redirect(manager.baseUrl() + manager.userHomePath());
        } catch (CouldNotObtainAccessToken error) {
            manager.write(error);
        }
    }

    private Token verifyAccessToken() throws CouldNotObtainAccessToken {
        Optional<Authentication> authentication = authenticationOf(manager.fromQuery("authId", String.class));

        if (!authentication.isPresent())
            return null;

        manager.currentSession().authId(manager.fromQuery("authId", String.class));

        String oauthVerifier = manager.fromQuery("oauth_verifier", String.class);
        return authentication.get().accessToken(Verifier.build(oauthVerifier));
    }

    private void listenForLogOut(AuthenticateCallbackAction action) {
        try {
            authService().addPushListener(accessToken(), pushListener(action));
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
            return authService().me(accessToken());
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
        user.roles(info.roleList());

        return user;
    }

}
