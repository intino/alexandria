package io.intino.alexandria.http.spark;

import io.intino.alexandria.Resource;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

@SuppressWarnings("unchecked")
public class SparkManager<P extends PushService> {
	private static final String XForwardedProto = "X-Forwarded-Proto";
	private static final String XForwardedPath = "X-Forwarded-Path";
	private static final String XForwardedPort = "X-Forwarded-Port";
	protected final P pushService;
	protected final Request request;
	protected final Response response;

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
		String sessionId = request.cookie(sessionCookieName());
		if (sessionId == null) sessionId = request.session().id();
		return (SparkSession) pushService.session(sessionId);
	}

	public void write(Object object) {
		new SparkWriter(response).write(object);
	}

	public void writeHeader(String name, String value) {
		new SparkWriter(response).writeHeader(name,value);
	}

	public void write(Object object, String name) {
		write(object, name, false);
	}

	public void write(Object object, String name, boolean embedded) {
		new SparkWriter(response).write(object, name, embedded);
	}

	public String fromHeader(String name) {
		return request.headers(name);
	}

	public String fromHeaderOrDefault(String name, String defaultValue) {
		return request.headers(name) == null ? defaultValue : request.headers(name);
	}

	public String fromQuery(String name) {
		return request.queryParams(name);
	}

	public String fromQueryOrDefault(String name, String defaultValue) {
		return request.queryParams(name) == null ? defaultValue : request.queryParams(name);
	}

	public String fromPath(String name) {
		return request.params(name);
	}

	public String fromPathOrDefault(String name, String defaultValue) {
		return request.params(name) == null ? defaultValue : request.params(name);
	}

	public String body(String name) {
		return request.body();
	}

	public Resource fromForm(String name) {
		try {
			Part part = request.raw().getPart(name);
			return part != null ?  new Resource(part.getName(), part.getInputStream()).metadata().contentType(part.getContentType()) : null;
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

	public String getHeader(String name) {
		return request.raw().getHeader(name);
	}

	public String realIp() {
		HttpServletRequest raw = request.raw();
		String ip = raw.getHeader("X-Real-IP");
		if (ip == null || ip.isEmpty()) ip = raw.getHeader("X-Forwarded-For");
		if (ip != null && !ip.isEmpty()) {
			int indexComma = ip.indexOf(',');
			if (indexComma == -1) return ip;
			else return ip.substring(0, indexComma);
		}
		return raw.getRemoteAddr();
	}

	private void setUpMultipartConfiguration() {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
		request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
	}

	private void setUpSessionCookiePath() {
//		HttpServletRequest request = this.request.raw();
//		HttpSession session = request.getSession();
//
//		if (request.getParameter("JSESSIONID") != null) {
//			Cookie userCookie = new Cookie("JSESSIONID", request.getParameter("JSESSIONID"));
//			response.raw().addCookie(userCookie);
//		} else {
//			String sessionId = session.getId();
//			Cookie userCookie = new Cookie("JSESSIONID", sessionId);
//			response.raw().addCookie(userCookie);
//		}
		HttpServletRequest request = this.request.raw();
		HttpSession session = request.getSession();
		String sessionCookieName = sessionCookieName();

		if (request.getParameter(sessionCookieName) != null) {
			Cookie userCookie = new Cookie(sessionCookieName, request.getParameter(sessionCookieName));
			response.raw().addCookie(userCookie);
		} else if (this.request.cookie(sessionCookieName) == null) {
			String sessionId = session.getId();
			Cookie userCookie = new Cookie(sessionCookieName, sessionId);
			response.raw().addCookie(userCookie);
		}
	}

	private String sessionCookieName() {
		String header = request.raw().getHeader(XForwardedPath);
		String sessionCookieName = header != null ? header.replace("/", "") : "federacion";
		if (sessionCookieName.isEmpty()) sessionCookieName = "federacion";
		return sessionCookieName;
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
		response.redirect(location);
	}

}
