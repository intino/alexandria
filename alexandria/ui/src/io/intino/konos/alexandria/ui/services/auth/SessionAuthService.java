package io.intino.konos.alexandria.ui.services.auth;

import io.intino.konos.alexandria.ui.services.AuthService;
import io.intino.konos.alexandria.ui.services.auth.exceptions.*;
import io.intino.konos.alexandria.ui.spark.UISparkManager;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionAuthService implements AuthService {
	protected UISparkManager manager;
	private static final Map<String, String> requestTokenMap = new HashMap<>();
	private static final Map<String, String> accessTokenMap = new HashMap<>();

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
	public AuthService.Authentication authenticate() throws SpaceAuthCallbackUrlIsNull {
		return new AuthService.Authentication() {

			@Override
			public Token requestToken() throws CouldNotObtainRequestToken {
				String token = requestTokenMap.getOrDefault(manager.currentSession().id(), null);
				return token != null ? Token.build(token) : null;
			}

			@Override
			public URL authenticationUrl(Token token) throws CouldNotObtainAuthorizationUrl {
				requestTokenMap.put(manager.currentSession().id(), token.id());
				return null;
			}

			@Override
			public Token accessToken() {
				String token = accessTokenMap.getOrDefault(manager.currentSession().id(), null);
				return token != null ? Token.build(token) : null;
			}

			@Override
			public Token accessToken(Verifier verifier) throws CouldNotObtainAccessToken {
				Token result = Token.build(UUID.randomUUID().toString());
				accessTokenMap.put(manager.currentSession().id(), result.id());
				return result;
			}

			@Override
			public void invalidate() throws CouldNotInvalidateAccessToken {
				if (manager == null) return;
				requestTokenMap.remove(manager.currentSession().id());
				accessTokenMap.remove(manager.currentSession().id());
			}
		};
	}

	@Override
	public boolean valid(Token token) {
		boolean valid = manager.currentSession().user() != null;
		if (token != null) accessTokenMap.put(manager.currentSession().id(), token.id());
		return valid;
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
