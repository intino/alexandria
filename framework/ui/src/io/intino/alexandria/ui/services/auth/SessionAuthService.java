package io.intino.alexandria.ui.services.auth;

import io.intino.alexandria.ui.services.AuthService;
import io.intino.alexandria.ui.services.auth.exceptions.*;
import io.intino.alexandria.ui.spark.UISparkManager;

import java.net.URL;

public class SessionAuthService implements AuthService {
	protected UISparkManager manager;

	public void inject(UISparkManager manager) {
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
	public Authentication authenticate() throws SpaceAuthCallbackUrlIsNull {
		return new Authentication() {

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

			@Override
			public Version version() {
				return Version.OAuth1;
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
	public String logoutUrl() {
		return null;
	}

	@Override
	public void addPushListener(Token token, FederationNotificationListener federationNotificationListener) throws CouldNotObtainInfo {
	}

}
