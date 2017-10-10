package io.intino.konos.server.activity.spark.resources;

import io.intino.konos.exceptions.KonosException;
import io.intino.konos.server.activity.displays.Display;
import io.intino.konos.server.activity.displays.DisplayNotifier;
import io.intino.konos.server.activity.displays.DisplayNotifierProvider;
import io.intino.konos.server.activity.displays.MessageCarrier;
import io.intino.konos.server.activity.services.AuthService;
import io.intino.konos.server.activity.services.AuthService.Authentication;
import io.intino.konos.server.activity.services.auth.Space;
import io.intino.konos.server.activity.services.auth.exceptions.CouldNotInvalidateAccessToken;
import io.intino.konos.server.activity.services.auth.exceptions.CouldNotObtainAuthorizationUrl;
import io.intino.konos.server.activity.services.auth.exceptions.CouldNotObtainRequestToken;
import io.intino.konos.server.activity.services.auth.exceptions.SpaceAuthCallbackUrlIsNull;
import io.intino.konos.server.activity.services.push.ActivityClient;
import io.intino.konos.server.activity.services.push.ActivitySession;
import io.intino.konos.server.activity.services.push.Browser;
import io.intino.konos.server.activity.spark.ActivitySparkManager;
import io.intino.konos.server.activity.utils.RequestHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public abstract class Resource implements io.intino.konos.server.Resource {
	private final DisplayNotifierProvider notifierProvider;
	protected final ActivitySparkManager manager;

	static final Map<String, String> authenticationIdMap = new HashMap<>();
	static final Map<String, Authentication> authenticationMap = new HashMap<>();

	public Resource(ActivitySparkManager manager, DisplayNotifierProvider notifierProvider) {
		this.manager = manager;
		this.notifierProvider = notifierProvider;
	}

	@Override
	public void execute() throws KonosException {
		fillBrowser(manager);
	}

	protected boolean isLogged() {
		if (!isFederated()) return true;

		String authId = manager.fromQuery("authId", String.class);
		Authentication authentication = authenticationOf(authId).orElse(null);
		return authentication != null && manager.authService().valid(authentication.accessToken());
	}

	protected synchronized void authenticate() {
		manager.redirect(authenticate(manager.baseUrl()));
	}

	protected synchronized String authenticate(String baseUrl) {
		String authId = UUID.randomUUID().toString();
		Space space = space();
		space.setAuthId(authId);
		space.setBaseUrl(baseUrl);
		saveAuthenticationId(authId);
		Authentication authentication = createAuthentication(authId);
		return authenticate(authentication);
	}

	protected void logout() {
		Optional<Authentication> authentication = authentication(this.manager.currentSession().id());

		if (!authentication.isPresent())
			return;

		try {
			authentication.get().invalidate();
			removeAuthentication(manager.currentSession().id());
		} catch (CouldNotInvalidateAccessToken error) {
			error.printStackTrace();
		}
	}

	protected DisplayNotifier notifier(ActivitySession session, ActivityClient client, Display display) {
		return notifierProvider.agent(display, carrier(session, client));
	}

	Optional<Authentication> authentication() {
		return authenticationOf(manager.fromQuery("authId", String.class));
	}

	Optional<Authentication> authentication(String sessionId) {
		String authenticationId = authenticationIdMap.get(sessionId);
		return Optional.ofNullable(authenticationMap.get(authenticationId));
	}

	Optional<Authentication> authenticationOf(String authenticationId) {
		return Optional.ofNullable(authenticationMap.get(locateAuthenticationId(authenticationId)));
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

	private Space space() {
		AuthService authService = manager.authService();
		return authService != null ? authService.space() : null;
	}

	private boolean isFederated() {
		return manager.authService() != null;
	}

	private void registerAuthentication(String authenticationId, Authentication authentication) {
		authenticationMap.put(authenticationId, authentication);
	}

	private String locateAuthenticationId(String authenticationId) {

		if (authenticationId != null && !authenticationId.isEmpty()) {
			saveAuthenticationId(authenticationId);
			return authenticationId;
		}

		return authenticationIdMap.get(manager.currentSession().id());
	}

	private void saveAuthenticationId(String authenticationId) {
		authenticationIdMap.put(manager.currentSession().id(), authenticationId);
	}

	private Authentication createAuthentication(String authenticationId) {
		try {
			registerAuthentication(authenticationId, manager.authService().authenticate());
			return authenticationOf(authenticationId).get();
		} catch (SpaceAuthCallbackUrlIsNull spaceAuthCallbackUrlIsNull) {
			spaceAuthCallbackUrlIsNull.printStackTrace();
			return null;
		}
	}

	private String authenticate(Authentication authentication) {
		try {
			return RequestHelper.post(authentication.authenticationUrl(authentication.requestToken())).toString();
		} catch (CouldNotObtainAuthorizationUrl | CouldNotObtainRequestToken | IOException e) {
			LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME).error(e.getMessage(), e);
			return null;
		}
	}

	private String errorPageUrl() {
		return manager.baseUrl() + "/error";
	}

	private MessageCarrier carrier(ActivitySession session, ActivityClient client) {
		return new MessageCarrier(manager.pushService(), session, client);
	}

	private void fillBrowser(ActivitySparkManager manager) {
		Browser browser = manager.currentSession().browser();
		browser.baseUrl(manager.baseUrl());
		browser.homeUrl(manager.baseUrl());
		browser.userHomeUrl(manager.baseUrl() + manager.userHomePath());
		browser.language(manager.languageFromUrl());
		browser.metadataLanguage(manager.languageFromHeader());
	}

}

