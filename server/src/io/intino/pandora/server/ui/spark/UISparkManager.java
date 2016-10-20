package io.intino.pandora.server.ui.spark;

import io.intino.pandora.server.ui.pushservice.PushService;
import io.intino.pandora.server.ui.pushservice.UIClient;
import io.intino.pandora.server.ui.pushservice.UISession;
import spark.Request;
import spark.Response;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class UISparkManager extends io.intino.pandora.server.spark.SparkManager {
	private PushService pushService;

	private static final String XForwardedProto = "X-Forwarded-Proto";
	private static final String XForwardedPath = "X-Forwarded-Path";

	public UISparkManager(Request request, Response response, PushService pushService) {
		super(request, response);
		this.pushService = pushService;
	}

	public PushService pushService() {
		return this.pushService;
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

	public UISession currentSession() {
		return pushService.session(request.session().id());
	}

	public UIClient client(String id) {
		return pushService.client(id);
	}

	public UIClient currentClient() {
		return pushService.currentClient();
	}

	public String baseUrl() {
		String result = generateBaseUrl();

		result = addHeaderProtocol(result);
		result = addHeaderPath(result);

		return result;
	}

	public String languageFromHeader() {
		String language = request.raw().getHeader("Accept-Language");
		return language != null ? languageOf(language.split(",")[0]) : null;
	}

	public String languageFromUrl() {
		return languageOf(request.queryParams("language"));
	}

	private String generateBaseUrl() {

		try {
			URL url = new URL(request.raw().getRequestURL().toString());
			String baseUrl = url.getProtocol() + "://" + url.getHost();

			int port = url.getPort();
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

	private String languageOf(String language) {
		if (language == null) return null;
		return Locale.forLanguageTag(language).toString().replaceAll("_.*", "");
	}

}
