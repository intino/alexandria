package io.intino.alexandria.ui.spark.resources;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.logger.Logger;
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

            UserInfo info = userInfo();
            if (info != null) action.whenLoggedIn(userOf(info));

            manager.redirect(manager.baseUrl() + manager.userHomePath());
        } catch (CouldNotObtainAccessToken error) {
            manager.write(error);
        }
    }

    private void verifyAccessToken() throws CouldNotObtainAccessToken {
        Optional<Authentication> authentication = authenticationOf(manager.currentSession(), manager.fromQuery("authId"));

        if (!authentication.isPresent())
            return;

        String oauthVerifier = manager.fromQuery("oauth_verifier");
        Token accessToken = authentication.get().accessToken(Verifier.build(oauthVerifier));
        manager.currentSession().token(accessToken);
    }

    private void listenForLogOut(AuthenticateCallbackAction action) {
        try {
            Token accessToken = accessToken();
            if (accessToken == null) return;
            authService().addPushListener(accessToken, pushListener(action));
        } catch (CouldNotObtainInfo error) {
            error.printStackTrace();
        }
    }

    @Override
    protected Token accessToken() {
        Authentication authentication = authentication().orElse(null);
        return authentication != null ? authentication.accessToken() : null;
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
            Token accessToken = accessToken();
            return accessToken != null ? authService().me(accessToken) : null;
        } catch (CouldNotObtainInfo error) {
            Logger.debug(error.getMessage());
            return null;
        }
    }

}
