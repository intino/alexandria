package io.intino.alexandria.ui.spark.resources;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.services.AuthService;
import io.intino.alexandria.ui.services.AuthService.Authentication;
import io.intino.alexandria.ui.services.auth.Token;
import io.intino.alexandria.ui.services.auth.UserInfo;
import io.intino.alexandria.ui.services.auth.Verifier;
import io.intino.alexandria.ui.services.auth.exceptions.CouldNotObtainAccessToken;
import io.intino.alexandria.ui.services.auth.exceptions.CouldNotObtainInfo;
import io.intino.alexandria.ui.spark.AuthenticateCallbackAction;
import io.intino.alexandria.ui.spark.UISparkManager;

import java.util.Optional;

public class AuthenticateCallbackResource extends Resource {

    public AuthenticateCallbackResource(UISparkManager manager, DisplayNotifierProvider notifierProvider) {
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

    private void verifyAccessToken() throws CouldNotObtainAccessToken {
        Optional<Authentication> authentication = authenticationOf(manager.fromQuery("authId", String.class));

        if (!authentication.isPresent())
            return;

        String oauthVerifier = manager.fromQuery("oauth_verifier", String.class);
        Token accessToken = authentication.get().accessToken(Verifier.build(oauthVerifier));
        manager.currentSession().token(accessToken);
    }

    private void listenForLogOut(AuthenticateCallbackAction action) {
        try {
            authService().addPushListener(accessToken(), pushListener(action));
        } catch (CouldNotObtainInfo error) {
            error.printStackTrace();
        }
    }

    @Override
    protected Token accessToken() {
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

}
