package io.intino.konos.alexandria.ui.spark;

import io.intino.konos.alexandria.ui.services.AuthService;
import io.intino.konos.alexandria.ui.services.EditorService;
import io.intino.konos.alexandria.ui.services.push.PushService;
import io.intino.konos.alexandria.ui.services.push.UIClient;
import io.intino.konos.alexandria.ui.services.push.UISession;
import spark.Request;
import spark.Response;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class UISparkManager extends io.intino.konos.alexandria.rest.spark.SparkManager {
	private final AuthService authService;
	private final EditorService editorService;
	private final boolean hasUserHome;

	public static final String KonosUserHomePath = "/konos/user";

	public UISparkManager(Request request, Response response, PushService pushService, AuthService authService, EditorService editorService, boolean hasUserHome) {
		super(pushService, request, response);
		this.authService = authService;
		this.editorService = editorService;
		this.hasUserHome = hasUserHome;
	}

	public io.intino.konos.alexandria.ui.services.push.PushService pushService() {
		return (PushService) this.pushService;
	}

	public AuthService authService() {
		return this.authService;
	}

	public EditorService editorService() {
		return this.editorService;
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
		return baseUrl() + this.request.raw().getPathInfo();
	}

	public UISession currentSession() {
		return (UISession) pushService.session(request.session().id());
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

	private String languageOf(String language) {
		if (language == null) return null;
		return Locale.forLanguageTag(language).toString().replaceAll("_.*", "");
	}

}
