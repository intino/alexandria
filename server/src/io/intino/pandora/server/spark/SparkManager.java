package io.intino.pandora.server.spark;

import io.intino.pandora.server.pushservice.Client;
import io.intino.pandora.server.pushservice.Session;
import io.intino.pandora.server.pushservice.SessionProvider;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

@SuppressWarnings("unchecked")
public class SparkManager {
	private final Request request;
	private final Response response;
	private final SessionProvider sessionProvider;

	private static final String XForwardedProto = "X-Forwarded-Proto";
	private static final String XForwardedPath = "X-Forwarded-Path";

	public SparkManager(Request request, Response response, SessionProvider sessionProvider) {
		this.request = request;
		this.response = response;
		this.sessionProvider = sessionProvider;
		setUpMultipartConfiguration();
	}

	public void write(Object object) {
		new SparkWriter(response).write(object);
	}

	public <S extends Session> S currentSession() {
		return sessionProvider.session(request.session().id());
	}

	public <C extends Client> C client(String id) {
		return sessionProvider.client(id);
	}

	public <C extends Client> C currentClient() {
		return sessionProvider.currentClient();
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

	public <T> T fromHeader(String name, Class<T> type) {
		return SparkReader.read(request.headers(name), type);
	}

	public <T> T fromQuery(String name, Class<T> type) {
		return SparkReader.read(request.queryParams(name), type);
	}

	public <T> T fromPath(String name, Class<T> type) {
		return SparkReader.read(request.params(name), type);
	}

	public <T> T fromBody(String name, Class<T> type) {
		return SparkReader.read(request.body(), type);
	}

	public <T> T fromForm(String name, Class<T> type) {
		return readPart(type);
	}

	private <T> T readPart(Class<T> type) {
		try {
			Part part = request.raw().getPart("content");
			if (part == null) return null;
			return SparkReader.read(part.getInputStream(), type);
		} catch (IOException | ServletException e) {
			return null;
		}
	}

	private void setUpMultipartConfiguration() {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
		request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
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
