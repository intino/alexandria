package io.intino.alexandria.ui.spark;

import io.intino.alexandria.http.spark.SparkManager;
import io.intino.alexandria.ui.services.AuthService;
import io.intino.alexandria.ui.services.push.PushService;
import io.intino.alexandria.ui.services.push.UIClient;
import io.intino.alexandria.ui.services.push.UISession;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class UISparkManager extends SparkManager<PushService> {
	private final AuthService authService;
	private final boolean hasUserHome;

	public static final String KonosUserHomePath = "/konos/user";

	public UISparkManager(Request request, Response response, PushService pushService, AuthService authService, boolean hasUserHome) {
		super(pushService, request, response);
		this.authService = authService;
		this.hasUserHome = hasUserHome;
	}

	public AuthService authService() {
		return this.authService;
	}

	public void linkToThread(UIClient client) {
		pushService.linkToThread(client);
	}

	public void unlinkFromThread() {
		pushService.unlinkFromThread();
	}

	public void unRegister(UIClient client) {
		pushService.unRegister(client);
	}

	public String userHomePath() {
		return hasUserHome ? KonosUserHomePath : "";
	}

	public String requestUrl() {
		String result = baseUrl();
		if (!result.endsWith("/")) result += "/";
		String requestUri = this.request.raw().getRequestURI();
		result += (requestUri != null && requestUri.startsWith("/") ? requestUri.substring(1) : requestUri);
		return withQueryString(result);
	}

	private String withQueryString(String result) {
		String queryString = this.request.raw().getQueryString();
		if (queryString == null || queryString.isEmpty()) return result;
		return result + "?" + queryString;
	}

	public UISession currentSession() {
		return (UISession) super.currentSession();
	}

	public UISession session(String id) {
		return (UISession) pushService().session(id);
	}

	public UIClient client(String id) {
		return (UIClient) pushService.client(id);
	}

	public UIClient currentClient() {
		return (UIClient) pushService.currentClient();
	}

	public String languageFromHeader() {
		String languageMetadata = request.raw().getHeader("Accept-Language");
		if (languageMetadata == null) return null;
		return languageOf(languageMetadata.split(",")[0]);
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

	private String languageOf(String language) {
		if (language == null || language.isEmpty()) return null;
		Locale locale = Locale.forLanguageTag(language);
		return (locale.getCountry().isEmpty() ? locale.getLanguage() : locale.getCountry()).toLowerCase();
	}

	public java.util.Map<String, String> cookies() {
		return request.cookies();
	}

	@Override
	public void redirect(String location) {
		currentSession().browser().add("callback", requestUrl());
		super.redirect(location);
	}
}
