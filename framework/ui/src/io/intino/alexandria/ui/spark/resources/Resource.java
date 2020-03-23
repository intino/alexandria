package io.intino.alexandria.ui.spark.resources;

import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.http.pushservice.MessageCarrier;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifier;
import io.intino.alexandria.ui.displays.notifiers.DisplayNotifierProvider;
import io.intino.alexandria.ui.services.AuthService;
import io.intino.alexandria.ui.services.AuthService.Authentication;
import io.intino.alexandria.ui.services.auth.SessionAuthService;
import io.intino.alexandria.ui.services.auth.Space;
import io.intino.alexandria.ui.services.auth.Token;
import io.intino.alexandria.ui.services.auth.UserInfo;
import io.intino.alexandria.ui.services.auth.exceptions.*;
import io.intino.alexandria.ui.services.push.Browser;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.services.push.UISession;
import io.intino.alexandria.ui.services.push.User;
import io.intino.alexandria.ui.spark.UISparkManager;
import io.intino.alexandria.ui.utils.RequestHelper;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public abstract class Resource implements io.intino.alexandria.http.Resource {
	static final Map<String, String> authenticationIdMap = new HashMap<>();
	static final Map<String, Authentication> authenticationMap = new HashMap<>();
	protected final UISparkManager manager;
	private final DisplayNotifierProvider notifierProvider;

	public Resource(UISparkManager manager, DisplayNotifierProvider notifierProvider) {
		this.manager = manager;
		this.notifierProvider = notifierProvider;
	}

	@Override
	public void execute() throws AlexandriaException {
		fillBrowser(manager);
	}

	protected void fillDeviceParameter() {
		String device = parameterValue("device");
		if (device != null && !device.isEmpty()) manager.currentSession().device(device);
	}

	protected String parameterValue(String key) {
		String value = manager.fromPath(key);
		if (value == null || value.isEmpty()) value = manager.fromQuery(key);
		if (value == null || value.isEmpty()) return null;

		try {
			if (value.startsWith("enc:"))
				return new String(Base64.getDecoder().decode(value.replace("enc:", "")));
			else
				return value;
		} catch (IllegalArgumentException ex) {
			return value;
		}
	}

	protected boolean isLogged() {
		if (!isFederated()) return true;

		String authId = manager.fromQuery("authId");
		Authentication authentication = authenticationOf(manager.currentSession(), authId).orElse(null);
		if (authentication == null) {

			return false;
		}

		return isLogged(authentication.accessToken());
	}

	protected boolean isLogged(Token accessToken) {
		try {
			if (!isFederated()) return true;
			AuthService authService = authService();
			if (!authService.valid(accessToken)) return false;
			authenticate(manager.currentSession(), accessToken);
			return true;
		} catch (CouldNotObtainInfo e) {
			Logger.debug(e.getMessage());
			return false;
		}
	}

	protected synchronized void authenticate() {
		String authenticate = authenticate(manager.currentSession(), manager.baseUrl());
		if (authenticate == null) authenticate = manager.baseUrl();
		manager.redirect(authenticate);
	}

	protected synchronized String authenticate(UISession session, String baseUrl) {
		String authId = UUID.randomUUID().toString();
		Space space = space();
		space.setAuthId(authId);
		space.setBaseUrl(baseUrl);
		saveAuthenticationId(session, authId);
		Authentication authentication = createAuthentication(session, authId);
		return authenticate(authentication);
	}

	protected synchronized void authenticate(UISession session, Token accessToken) throws CouldNotObtainInfo {
		if (!isFederated()) return;
		UserInfo info = authService().me(accessToken);
		session.user(userOf(info));
		session.token(accessToken);
	}

	protected void logout(UISession session) {
		Optional<Authentication> authentication = authentication(session.id());

		if (!authentication.isPresent())
			return;

		try {
			authentication.get().invalidate();
			removeAuthentication(session.id());
		} catch (CouldNotInvalidateAccessToken error) {
			Logger.debug(error.getMessage());
		}
	}

	protected DisplayNotifier notifier(UISession session, UIClient client, Display display) {
		return notifierProvider.notifier(display, carrier(session, client));
	}

	protected Token accessToken() {
		return Token.build(manager.fromQuery("token"));
	}

	protected void fillBrowser(UISparkManager manager, UISession session) {
		Browser browser = session.browser();
		browser.baseUrl(manager.baseUrl());
		browser.basePath(manager.basePath());
		browser.homeUrl(manager.baseUrl());
		browser.userHomeUrl(manager.baseUrl() + manager.userHomePath());
		browser.language(manager.languageFromUrl());
		browser.metadataLanguage(manager.languageFromHeader());
		browser.metadataIpAddress(manager.ipAddressFromHeader());
	}

	Optional<Authentication> authentication() {
		return authenticationOf(manager.currentSession(), manager.fromQuery("authId"));
	}

	Optional<Authentication> authentication(String sessionId) {
		String authenticationId = authenticationIdMap.get(sessionId);
		return Optional.ofNullable(authenticationMap.get(authenticationId));
	}

	Optional<Authentication> authenticationOf(UISession session, String authenticationId) {
		return Optional.ofNullable(authenticationMap.get(locateAuthenticationId(session, authenticationId)));
	}

	void removeAuthentication(String sessionId) {
		String authenticationId = authenticationIdMap.get(sessionId);
		authenticationIdMap.remove(sessionId);
		authenticationMap.remove(authenticationId);
	}

	String authenticationId() {
		String sessionId = manager.currentSession().id();
		return authenticationIdMap.containsKey(sessionId) ? authenticationIdMap.get(sessionId) : null;
	}

	String home() {
		return manager.baseUrl();
	}

	AuthService authService() {
		AuthService authService = manager.authService();

		if (authService == null) return null;
		if (authService instanceof SessionAuthService)
			((SessionAuthService) authService).inject(manager);

		return authService;
	}

	User userOf(UserInfo info) {
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

	private Space space() {
		AuthService authService = authService();
		return authService != null ? authService.space() : null;
	}

	private boolean isFederated() {
		return authService() != null;
	}

	private void registerAuthentication(String authenticationId, Authentication authentication) {
		authenticationMap.put(authenticationId, authentication);
	}

	private String locateAuthenticationId(UISession session, String authenticationId) {

		if (authenticationId != null && !authenticationId.isEmpty()) {
			saveAuthenticationId(session, authenticationId);
			return authenticationId;
		}

		return authenticationIdMap.get(manager.currentSession().id());
	}

	private void saveAuthenticationId(UISession session, String authenticationId) {
		authenticationIdMap.put(session.id(), authenticationId);
	}

	private Authentication createAuthentication(UISession session, String authenticationId) {
		try {
			registerAuthentication(authenticationId, authService().authenticate());
			return authenticationOf(session, authenticationId).get();
		} catch (SpaceAuthCallbackUrlIsNull error) {
			Logger.debug(error.getMessage());
			return null;
		}
	}

	private String authenticate(Authentication authentication) {
		try {
			URL url = authentication.authenticationUrl(authentication.requestToken());
			if (url == null) return null;
			return RequestHelper.post(url).toString();
		} catch (CouldNotObtainAuthorizationUrl | CouldNotObtainRequestToken | IOException e) {
			Logger.debug(e.getMessage());
			return null;
		}
	}

	private String errorPageUrl() {
		return manager.baseUrl() + "/error";
	}

	private MessageCarrier carrier(UISession session, UIClient client) {
		return new MessageCarrier(manager.pushService(), session, client);
	}

	private void fillBrowser(UISparkManager manager) {
		fillBrowser(manager, manager.currentSession());
	}

}

