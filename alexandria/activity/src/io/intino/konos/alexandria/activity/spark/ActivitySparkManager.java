package io.intino.konos.alexandria.activity.spark;

import io.intino.konos.alexandria.activity.services.AuthService;
import io.intino.konos.alexandria.activity.services.push.ActivityClient;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;
import io.intino.konos.alexandria.activity.services.push.PushService;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class ActivitySparkManager extends io.intino.konos.alexandria.rest.spark.SparkManager {
	private final PushService pushService;
	private final AuthService authService;
	private final boolean hasUserHome;

	public static final String KonosUserHomePath = "/konos/user";

	public ActivitySparkManager(Request request, Response response, PushService pushService, AuthService authService, boolean hasUserHome) {
		super(request, response);
		this.pushService = pushService;
		this.authService = authService;
		this.hasUserHome = hasUserHome;
	}

	public PushService pushService() {
		return this.pushService;
	}

	public AuthService authService() {
		return this.authService;
	}

	public void linkToThread(ActivityClient client) {
		pushService.linkToThread(client);
	}

	public void unlinkFromThread() {
		pushService.unlinkFromThread();
	}

	public void unRegister(ActivityClient client) {
		pushService.unRegister(client);
	}

	public ActivitySession currentSession() {
		return pushService.session(request.session().id());
	}

	public ActivityClient client(String id) {
		return pushService.client(id);
	}

	public ActivityClient currentClient() {
		return pushService.currentClient();
	}

	public String userHomePath() {
		return hasUserHome ? KonosUserHomePath : "";
	}

	public String requestUrl() {
		return baseUrl() + this.request.raw().getPathInfo();
	}

	public String languageFromHeader() {
		String language = request.raw().getHeader("Accept-Language");
		return language != null ? languageOf(language.split(",")[0]) : null;
	}

	public String ipAddressFromHeader() {
		HttpServletRequest raw = request.raw();

		String ipAddress = raw.getHeader("Remote_Addr");
		if (ipAddress == null || ipAddress.isEmpty())
			ipAddress = raw.getHeader("HTTP_X_FORWARDED_FOR");

		if (ipAddress != null && !ipAddress.isEmpty())
			return ipAddress;

		return raw.getRemoteAddr();
	}

	public String languageFromUrl() {
		return languageOf(request.queryParams("language"));
	}

	public void redirect(String location) {
		response.redirect(location);
	}

	private String languageOf(String language) {
		if (language == null) return null;
		return Locale.forLanguageTag(language).toString().replaceAll("_.*", "");
	}

}
