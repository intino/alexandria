package io.intino.konos.alexandria.activity.services.auth;

import io.intino.konos.alexandria.activity.services.AuthService;
import io.intino.konos.alexandria.activity.services.auth.exceptions.*;
import io.intino.konos.alexandria.activity.spark.ActivitySparkManager;

import java.net.URL;

public class SessionAuthService implements AuthService {
	private ActivitySparkManager manager;

	public void inject(ActivitySparkManager manager) {
		this.manager = manager;
	}

	@Override
	public URL url() {
		return null;
	}

	@Override
	public Space space() {
		return new Space(null);
	}

	@Override
	public AuthService.Authentication authenticate() throws SpaceAuthCallbackUrlIsNull {
		return new AuthService.Authentication() {

			@Override
			public Token requestToken() throws CouldNotObtainRequestToken {
				return null;
			}

			@Override
			public URL authenticationUrl(Token token) throws CouldNotObtainAuthorizationUrl {
				return null;
			}

			@Override
			public Token accessToken() {
				return null;
			}

			@Override
			public Token accessToken(Verifier verifier) throws CouldNotObtainAccessToken {
				return null;
			}

			@Override
			public void invalidate() throws CouldNotInvalidateAccessToken {
			}

			private void createPair() {
			}
		};
	}

	@Override
	public boolean valid(Token token) {
		return manager.currentSession().user() != null;
	}

	@Override
	public FederationInfo info(Token token) throws CouldNotObtainInfo {
		return null;
	}

	@Override
	public UserInfo me(Token token) throws CouldNotObtainInfo {
		return null;
	}

	@Override
	public void logout(Token token) throws CouldNotLogout {
	}

	@Override
	public void addPushListener(Token token, AuthService.FederationNotificationListener federationNotificationListener) throws CouldNotObtainInfo {
	}

}
