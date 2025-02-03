package io.intino.alexandria.http.server;

import io.intino.alexandria.Resource;
import io.intino.alexandria.http.pushservice.Client;
import io.intino.alexandria.http.pushservice.PushService;
import io.intino.alexandria.http.pushservice.Session;
import io.intino.alexandria.logger.Logger;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class AlexandriaHttpManager<P extends PushService<? extends Session<?>, ? extends Client>> {
	private static final String XForwardedProto = "X-Forwarded-Proto";
	private static final String XForwardedHost = "X-Forwarded-Host";
	private static final String XForwardedPath = "X-Forwarded-Path";
	private static final String XForwardedPort = "X-Forwarded-Port";
	protected final P pushService;
	protected final AlexandriaHttpRequest request;
	protected final AlexandriaHttpResponse response;
	private final AlexandriaHttpResourceProvider resourceProvider;

	public AlexandriaHttpManager(P pushService, AlexandriaHttpRequest request, AlexandriaHttpResponse response, AlexandriaHttpResourceProvider resourceProvider) {
		this.pushService = pushService;
		this.request = request;
		this.response = response;
		this.resourceProvider = resourceProvider;
		setUpMultipartConfiguration();
		setUpSessionCookiePath();
	}

	public P pushService() {
		return this.pushService;
	}

	public AlexandriaHttpSession<?> currentSession() {
		String sessionId = request.cookie(sessionCookieName());
		if (sessionId == null) sessionId = request.raw().getSession().getId();
		return (AlexandriaHttpSession<?>) pushService.session(sessionId);
	}

	public void write(Object object) {
		new AlexandriaHttpWriter(response).write(object);
	}

	public void writeHeader(String name, String value) {
		new AlexandriaHttpWriter(response).writeHeader(name, value);
	}

	public void write(Object object, String name) {
		write(object, name, false);
	}

	public void write(Object object, String name, boolean embedded) {
		new AlexandriaHttpWriter(response).write(object, name, embedded);
	}

	public String fromHeader(String name) {
		return request.header(name);
	}

	public String fromHeaderOrDefault(String name, String defaultValue) {
		return request.header(name) == null ? defaultValue : request.header(name);
	}

	public <X extends Throwable> String fromHeaderOrElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
		if (request.header(name) == null) throw exceptionSupplier.get();
		return request.header(name);
	}

	public String fromQuery(String name) {
		return request.queryParam(name);
	}

	public String fromQueryOrDefault(String name, String defaultValue) {
		return request.queryParam(name) == null ? defaultValue : request.queryParam(name);
	}

	public <X extends Throwable> String fromQueryOrElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
		if (request.queryParam(name) == null) throw exceptionSupplier.get();
		return request.queryParam(name);
	}

	public Map<String, String[]> formAndQueryParameters() {
		return request.raw().getParameterMap();
	}

	public String fromPath(String name) {
		return request.pathParam(name);
	}

	public String fromPathOrDefault(String name, String defaultValue) {
		return request.pathParam(name);
	}

	public <X extends Throwable> String fromPathOrElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
		return request.pathParam(name);
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
		return body.isEmpty() ? defaultValue : body;
	}

	public <X extends Throwable> String fromBodyOrElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
		final String body = request.body();
		if (body.isEmpty()) throw exceptionSupplier.get();
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
		return fromPartAsString(resourceProvider.resource(name));
	}

	public String fromFormOrDefault(String name, String defaultValue) {
		String content = fromPartAsString(resourceProvider.resource(name));
		return content != null ? content : defaultValue;
	}

	public <X extends Throwable> String fromFormAsStringOrElseThrow(String name, Supplier<? extends X> exceptionSupplier) throws X {
		String resource = fromPartAsString(resourceProvider.resource(name));
		if (resource == null) throw exceptionSupplier.get();
		return resource;
	}

	public List<Resource> fromPartsAsResources() {
		return resourceProvider.resources().stream()
				.filter(p -> !textContentType(p))
				.collect(Collectors.toList());
	}

	public List<String> fromPartsAsStrings() {
		return resourceProvider.resources().stream()
				.filter(this::textContentType)
				.map(this::fromPartAsString)
				.collect(Collectors.toList());
	}


	private Resource fromPartAsResource(String name) {
		return resourceProvider.resource(name);
	}

	private String fromPartAsString(Resource resource) {
		try {
			return resource != null ? new String(resource.bytes(), StandardCharsets.UTF_8) : null;
		} catch (IOException e) {
			return null;
		}
	}

	public AlexandriaHttpRequest request() {
		return request;
	}

	public AlexandriaHttpResponse response() {
		return response;
	}

	public String domain() {
		return buildDomainUrl();
	}

	public String baseUrl() {
		String result = buildBaseUrl();

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

	private boolean textContentType(Resource resource) {
		return "application/json".equals(resource.metadata().contentType()) || "text/plain".equals(resource.metadata().contentType());
	}

	private void setUpMultipartConfiguration() {
		//MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
		//request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
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

	private String buildBaseUrl() {
		try {
			URL requestUrl = new URL(request.raw().getRequestURL().toString());
			String forwardedHost = request.raw().getHeader(XForwardedHost);
			String forwardedProtocol = request.raw().getHeader(XForwardedProto);
			boolean forwardedUriDefined = forwardedProtocol != null && forwardedHost != null;
			return (forwardedUriDefined ? forwardedProtocol : requestUrl.getProtocol()) + "://" + buildDomainUrl();
		} catch (MalformedURLException e) {
			return null;
		}
	}

	private String buildDomainUrl() {
		try {
			URL requestUrl = new URL(request.raw().getRequestURL().toString());
			String forwardedHost = request.raw().getHeader(XForwardedHost);
			String forwardedProtocol = request.raw().getHeader(XForwardedProto);
			boolean forwardedUriDefined = forwardedProtocol != null && forwardedHost != null;
			String domain = forwardedUriDefined ? forwardedHost : requestUrl.getHost();

			int port = getHeaderPort();
			if (port == -1) port = requestUrl.getPort();
			if (port != 80 && port != -1) domain += ":" + port;

			return domain;
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

		return Integer.parseInt(forwardedPort);
	}

	public void redirect(String location) {
		try {
			response.raw().sendRedirect(location);
		} catch (IOException e) {
			Logger.error(e);
		}
	}

	private URL url(String value) {
		try {
			return new URI(value).toURL();
		} catch (MalformedURLException | URISyntaxException e) {
			Logger.error(e);
			return null;
		}
	}

}
