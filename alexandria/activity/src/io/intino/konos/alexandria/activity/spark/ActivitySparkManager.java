package io.intino.konos.alexandria.activity.spark;

import io.intino.konos.alexandria.activity.services.AuthService;
import io.intino.konos.alexandria.activity.services.push.ActivityClient;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;
import io.intino.konos.alexandria.activity.services.push.PushService;
import spark.Request;
import spark.Response;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class ActivitySparkManager extends io.intino.konos.alexandria.rest.spark.SparkManager {
	private final PushService pushService;
	private final AuthService authService;
	private final boolean hasUserHome;

	public static final String KonosUserHomePath = "/konos/user";

	private static final String XForwardedProto = "X-Forwarded-Proto";
	private static final String XForwardedPath = "X-Forwarded-Path";
	private static final String XForwardedPort = "X-Forwarded-Port";

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

	public String baseUrl() {
		String result = generateBaseUrl();

		result = addHeaderProtocol(result);
		result = addHeaderPath(result);

		return result;
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

	public String languageFromUrl() {
		return languageOf(request.queryParams("language"));
	}

	public void redirect(String location) {
		response.redirect(location);
	}

	private String generateBaseUrl() {

		try {
			URL url = new URL(request.raw().getRequestURL().toString());
			String baseUrl = url.getProtocol() + "://" + url.getHost();

			int port = getHeaderPort();
			if (port == -1) port = url.getPort();
			if (port != 80 && port != -1) baseUrl += ":" + port;

			return baseUrl;
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private String addHeaderProtocol(String url) {
		String forwardedProto = request.raw().getHeader(XForwardedProto);

		if (forwardedProto == null || !forwardedProto.equals("https"))
			return url;

		return url.replace("http:", "https:");
	}

	private String addHeaderPath(String url) {
		String forwardedPath = request.raw().getHeader(XForwardedPath);

		if (forwardedPath == null)
			return url;

		return url + (forwardedPath.equals("") || forwardedPath.equals("/") ? "" : forwardedPath);
	}

	private int getHeaderPort() {
		String forwardedPort = request.raw().getHeader(XForwardedPort);

		if (forwardedPort == null || forwardedPort.isEmpty())
			return -1;

		return Integer.valueOf(forwardedPort);
	}

	private String languageOf(String language) {
		if (language == null) return null;
		return Locale.forLanguageTag(language).toString().replaceAll("_.*", "");
	}

}
