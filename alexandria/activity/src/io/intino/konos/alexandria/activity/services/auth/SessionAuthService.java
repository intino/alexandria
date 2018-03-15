package io.intino.konos.alexandria.activity.services.auth;

import io.intino.konos.alexandria.activity.services.AuthService;
import io.intino.konos.alexandria.activity.services.auth.exceptions.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DefaultAuthService implements AuthService {
	private Map<String, String> tokenMap = new HashMap<>();

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
			private String requestToken = null;
			private String accessToken = null;

			@Override
			public Token requestToken() throws CouldNotObtainRequestToken {
				createPair();
				return () -> requestToken;
			}

			@Override
			public URL authenticationUrl(Token token) throws CouldNotObtainAuthorizationUrl {
				return null;
			}

			@Override
			public Token accessToken() {
				createPair();
				return () -> accessToken;
			}

			@Override
			public Token accessToken(Verifier verifier) throws CouldNotObtainAccessToken {
				return () -> accessToken;
			}

			@Override
			public void invalidate() throws CouldNotInvalidateAccessToken {
				tokenMap.remove(requestToken);
				requestToken = null;
				accessToken = null;
			}

			private void createPair() {
				if (requestToken != null) return;
				requestToken = UUID.randomUUID().toString();
				accessToken = UUID.randomUUID().toString();
				tokenMap.put(requestToken, accessToken);
			}
		};
	}

	@Override
	public boolean valid(Token token) {
		return tokenMap.values().stream().anyMatch(t -> t.equals(token.id()));
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
