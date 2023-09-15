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
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
		new SparkWriter(response).writeHeader(name, value);
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

	public <X extends Throwable> String fromHeaderOrElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
		if (request.headers(name) == null) throw exceptionSupplier.get();
		return request.headers(name);
	}

	public String fromQuery(String name) {
		return request.queryParams(name);
	}

	public String fromQueryOrDefault(String name, String defaultValue) {
		return request.queryParams(name) == null ? defaultValue : request.queryParams(name);
	}

	public <X extends Throwable> String fromQueryOrElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
		if (request.queryParams(name) == null) throw exceptionSupplier.get();
		return request.queryParams(name);
	}

	public Map<String, String[]> formAndQueryParameters() {
		return request.raw().getParameterMap();
	}

	public String fromPath(String name) {
		return request.params(name);
	}

	public String fromPathOrDefault(String name, String defaultValue) {
		return request.params(name) == null ? defaultValue : request.params(name);
	}

	public <X extends Throwable> String fromPathOrElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
		if (request.params(name) == null) throw exceptionSupplier.get();
		return request.params(name);
	}

	@Deprecated
	public String body(String value) {
		return request.body();
	}

	public String body() {
		return request.body();
	}

	public byte[] fromBodyAsBytes() {
		return request.bodyAsBytes();
	}

	public String fromBody() {
		return request.body();
	}

	public String fromBodyOrDefault(String defaultValue) {
		String body = request.body();
		return body == null || body.isEmpty() ? defaultValue : body;
	}

	public <X extends Throwable> String fromBodyOrElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
		final String body = request.body();
		if (body == null || body.isEmpty()) throw exceptionSupplier.get();
		return body;
	}

	public Resource fromFormAsResource(String name) {
		return fromPartAsResource(name);
	}

	public Resource fromFormAsResourceOrDefault(String name, Resource defaultValue) {
		Resource resource = fromPartAsResource(name);
		return resource != null ? resource : defaultValue;
	}

	public <X extends Throwable> Resource fromFormAsResourceOrElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
		Resource resource = fromPartAsResource(name);
		if (resource == null) throw exceptionSupplier.get();
		return resource;
	}

	public String fromFormAsString(String name) {
		return fromPartAsString(name);
	}

	public String fromFormOrDefault(String name, String defaultValue) {
		String content = fromPartAsString(name);
		return content != null ? content : defaultValue;
	}

	public <X extends Throwable> String fromFormAsStringOrElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
		String resource = fromPartAsString(name);
		if (resource == null) throw exceptionSupplier.get();
		return resource;
	}

	public List<Resource> fromPartsAsResources() {
		try {
			return request.raw().getParts().stream()
					.filter(p -> !textContentType(p))
					.map(p -> fromPartAsResource(p.getName()))
					.collect(Collectors.toList());
		} catch (ServletException | IOException e) {
			return Collections.emptyList();
		}
	}

	public List<String> fromPartsAsStrings() {
		try {
			return request.raw().getParts().stream()
					.filter(this::textContentType)
					.map(p -> fromPartAsString(p.getName()))
					.collect(Collectors.toList());
		} catch (ServletException | IOException e) {
			return Collections.emptyList();
		}
	}


	private Resource fromPartAsResource(String name) {
		try {
			Part part = request.raw().getPart(name);
			return part != null ? new Resource(part.getSubmittedFileName() == null ? part.getName() : part.getSubmittedFileName(), part.getInputStream()).metadata().contentType(part.getContentType()) : null;
		} catch (ServletException | IOException e) {
			return null;
		}
	}

	private String fromPartAsString(String name) {
		try {
			Part part = request.raw().getPart(name);
			return part != null ? new String(part.getInputStream().readAllBytes(), StandardCharsets.UTF_8) : null;
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

	public String ip() {
		return request.ip();
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

	private boolean textContentType(Part p) {
		return "application/json".equals(p.getContentType()) || "text/plain".equals(p.getContentType());
	}

	private void setUpMultipartConfiguration() {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
		request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
	}

	private void setUpSessionCookiePath() {
		HttpServletRequest request = this.request.raw();
		HttpSession session = request.getSession();
		String sessionCookieName = sessionCookieName();

		if (request.getParameter(sessionCookieName) != null) {
			Cookie userCookie = new Cookie(sessionCookieName, request.getParameter(sessionCookieName));
			userCookie.setHttpOnly(true);
			userCookie.setSecure(request.isSecure());
			response.raw().addCookie(userCookie);
		} else if (this.request.cookie(sessionCookieName) == null) {
			String cookiePath = request.getHeader(XForwardedPath);
			String sessionId = session.getId();
			Cookie userCookie = new Cookie(sessionCookieName, sessionId);
			userCookie.setHttpOnly(true);
			userCookie.setSecure(request.isSecure());
			userCookie.setPath(cookiePath);
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
