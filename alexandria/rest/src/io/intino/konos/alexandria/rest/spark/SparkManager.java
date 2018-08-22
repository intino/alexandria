package io.intino.konos.alexandria.rest.spark;

import io.intino.konos.alexandria.schema.Resource;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;

@SuppressWarnings("unchecked")
public class SparkManager<P extends PushService> {
	protected final P pushService;
	protected final Request request;
	protected final Response response;

	private static final String XForwardedProto = "X-Forwarded-Proto";
	private static final String XForwardedPath = "X-Forwarded-Path";
	private static final String XForwardedPort = "X-Forwarded-Port";

	public SparkManager(P pushService, Request request, Response response) {
		this.pushService = pushService;
		this.request = request;
		this.response = response;
		setUpMultipartConfiguration();
		setUpSessionCookiePath();
	}

	public P pushService() {
		return this.pushService;
	}

	public SparkSession currentSession() {
		return (SparkSession) pushService.session(request.session().id());
	}

	public void write(Object object) {
		new SparkWriter(response).write(object);
	}

	public void write(Object object, String name) {
		write(object, name, false);
	}

	public void write(Object object, String name, boolean embedded) {
		new SparkWriter(response).write(object, name, embedded);
	}

	public <T> T fromHeader(String name, Class<T> type) {
		return SparkReader.read(request.headers(name), type);
	}

	public <T> T fromQuery(String name, Class<T> type) {
		return SparkReader.read(request.queryParams(name), type);
	}

	public <T> T fromQuery(String name, Type type) {
		return SparkReader.read(request.queryParams(name), type);
	}

	public <T> T fromPath(String name, Class<T> type) {
		return SparkReader.read(request.params(name), type);
	}

	public <T> T fromBody(String name, Class<T> type) {
		try {
			if (type.isAssignableFrom(Resource.class) || type.isAssignableFrom(InputStream.class))
				return (T) new Resource(name).data(request.raw().getInputStream());

			return SparkReader.read(request.body(), type);
		} catch (IOException e) {
			return null;
		}
	}

	public <T> T fromForm(String name, Class<T> type) {
		try {
			Part part = request.raw().getPart(name);
			return part != null ? (T) new Resource(part.getName()).data(part.getInputStream()).contentType(part.getContentType()) : null;
		} catch (ServletException | IOException e) {
			return null;
		}
	}

	public Request request() {
		return request;
	}

	public Response response() {
		return response;
	}

	public String domain() {
		try {
			URL url = new URL(request.raw().getRequestURL().toString());
			String result = url.getHost();
			int port = getHeaderPort();
			if (port == -1) port = url.getPort();
			if (port != 80 && port != -1) result += ":" + port;
			return result;
		} catch (MalformedURLException e) {
			return null;
		}
	}

	public String baseUrl() {
		String result = generateBaseUrl();

		result = addHeaderProtocol(result);
		result = addHeaderPath(result);

		return result;
	}

	public String basePath() {
		String forwardedPath = request.raw().getHeader(XForwardedPath);
		return forwardedPath == null || forwardedPath.equals("") || forwardedPath.equals("/") ? "" : forwardedPath;
	}

	private void setUpMultipartConfiguration() {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
		request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
	}

	private void setUpSessionCookiePath() {
		HttpServletRequest request = this.request.raw();
		HttpSession session = request.getSession();

		if (request.getParameter("JSESSIONID") != null) {
			Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID"));
			response.raw().addCookie(userCookie);
		} else {
			String sessionId = session.getId();
			Cookie userCookie = new Cookie("JSESSIONID", sessionId);
			response.raw().addCookie(userCookie);
		}
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

	public void redirect(String location) {
		response.redirect(response.raw().encodeRedirectURL(location));
	}

}
