package org.siani.pandora.server.web.actions;

import org.siani.pandora.server.actions.Action;
import org.siani.pandora.server.actions.RequestAdapter;
import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static java.util.Collections.emptyList;

class RequestWrapper extends SparkWrapper {

	private static final String SessionId = "sessionId";
	private static final String Request = "request";
	private static final String RequestUrl = "requestUrl";
	private static final String BaseUrl = "baseUrl";
	private static final String LanguageFromUrl = "languageFromUrl";
	private static final String LanguageFromMetadata = "languageFromMetadata";
	private static final String Metadata = "metadata";
	private static final String Parameters = "parameters";
	private static final String Files = "files";
	private final Request request;

	public static final String XForwardedProto = "X-Forwarded-Proto";
	public static final String XForwardedHost = "X-Forwarded-Host";
	public static final String XForwardedPath = "X-Forwarded-Path";

	RequestWrapper(Request request) {
		this.request = request;
		setUpMultipartConfiguration();
	}

	@Override
	protected InvocationHandler handler() {
		return (Object proxy, Method method, Object[] args) -> {
			return mapParameterToMethod(method);
		};
	}

	private Object mapParameterToMethod(Method method) {
		String methodName = method.getName();

		try {
			Object value = (methodName.startsWith("header")) ? headerForName(method.getName()) : parameterForName(method.getName());

			if (value instanceof Map)
				return adapt(method.getName(), mapValueReturnType(method), (Map<String, Object>) value);

			return adapt(method.getName(), method.getReturnType(), value);
		} catch (IOException | ServletException e) {
			e.printStackTrace();
			throw new RuntimeException("Error receiving parameters");
		}
	}

	Class<?> mapValueReturnType(Method method) {
		return ((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[1].getClass();
	}

	private <T> Map<String, T> adapt(String methodName, Class<T> valueReturnType, Map<String, T> map) {
		Map<String, T> result = new HashMap<>();
		for (Map.Entry<String, T> mapEntry : map.entrySet())
			result.put(mapEntry.getKey(), adapt(methodName, valueReturnType, mapEntry.getValue()));
		return result;
	}

	@SuppressWarnings("unchecked")
	private <T> T adapt(String methodName, Class<T> returnType, Object value) {

		if (returnType == InputStream.class || isFileList(value))
			return (T) value;

		if (isFileList(value))
			return (T) value;

		if (returnType == Request.class)
			return (T) value;

		if (returnType == LocalDateTime.class)
			return (T) LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf((String)value)), ZoneId.of("UTC"));

		if (returnType == Double.class)
			return (T) Double.valueOf((String) value);

		if (returnType == Integer.class)
			return (T) Integer.valueOf((String) value);

		if (returnType == Boolean.class)
			return (T) Boolean.valueOf((String) value);

		if (returnType == String.class)
			return (T) value;

		RequestAdapter adapter = adapterFor(methodName, returnType);
		return methodName.endsWith("List") ? (T) adapter.adaptList((String) value) : (T) adapter.adapt((String) value);
	}

	private boolean isFileList(Object value) {
		if (!(value instanceof List)) return false;
		List<?> valueList = (List<?>) value;
		return valueList.size() > 0 && valueList.get(0) instanceof Action.Input.File;
	}

	private void setUpMultipartConfiguration() {
		MultipartConfigElement multipartConfigElement = new MultipartConfigElement(System.getProperty("java.io.tmpdir"));
		request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement);
	}

	private RequestAdapter adapterFor(String method, Class returnType) {
		return adapters.requestAdapterOf(method, returnType);
	}

	private String headerForName(String method) {
		return request.headers(normalizeHeaderName(method));
	}

	private String normalizeHeaderName(String headerName) {
		char[] header = headerName.replace("header", "").toCharArray();
		StringBuilder result = new StringBuilder();

		for (char character : header) {
			boolean upperCase = Character.isUpperCase(character);
			character = upperCase ? Character.toLowerCase(character) : character;
			String separator = upperCase && result.length() > 0 ? "-" : "";
			result.append(character).append(separator);
		}

		return result.toString();
	}

	private Object parameterForName(String method) throws IOException, ServletException {
		if (method.equals(SessionId))
			return request.session().id();

		if (method.equals(RequestUrl))
			return requestUrl();

		if (method.equals(BaseUrl))
			return baseUrl();

		if (method.equals(LanguageFromUrl))
			return languageFromUrl();

		if (method.equals(LanguageFromMetadata))
			return languageFromHeader();

		if (method.equals(Metadata))
			return metadata();

		if (method.equals(Parameters))
			return parameters();

		if (method.equals(Files))
			return files();

		if (method.equals(Request))
			return request;

		if (isMultipart(method))
			return readPart(method);

		if (request.queryParams(method) != null)
			return request.queryParams(method);

		if (request.params(method) != null)
			return request.params(method);

		return request.body() != null ? request.body() : null;
	}

	private Map<String, String> parameters() {
		Map<String, String> result = new HashMap<>();

		request.queryParams().stream().forEach(param -> result.put(param, request.queryParams(param)));
		request.params().keySet().stream().forEach(param -> result.put(param.substring(1), request.params().get(param)));

		return result;
	}

	private List<Action.Input.File> files() throws IOException, ServletException {
		if (!isMultipart())
			return emptyList();

		List<Action.Input.File> result = new ArrayList<>();
		request.raw().getParts().forEach(part -> {
			if (part.getContentType() == null) return;

			result.add(new Action.Input.File() {
				@Override
				public String name() {
					return part.getSubmittedFileName();
				}

				@Override
				public int size() {
					return (int) part.getSize();
				}

				@Override
				public String contentType() {
					return part.getContentType();
				}

				@Override
				public InputStream content() throws IOException {
					return part.getInputStream();
				}
			});
		});

		return result;
	}

	private String requestUrl() {
		return addHeaderProtocol(request.raw().getRequestURL().toString());
	}

	private String baseUrl() {
		String result = generateBaseUrl();

		result = addHeaderProtocol(result);
		result = addHeaderPath(result);

		return result;
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

	private String languageFromUrl() {
		return languageOf(request.queryParams("language"));
	}

	private Map<String, String> metadata() {
		Map<String, String> result = new HashMap<>();
		Enumeration<String> headerNames = request.raw().getHeaderNames();

		while (headerNames.hasMoreElements()) {
			String name = headerNames.nextElement();
			result.put(name, request.raw().getHeader(name));
		}

		return result;
	}

	private String languageFromHeader() {
		String language = request.raw().getHeader("Accept-Language");
		return language != null ? languageOf(language.split(",")[0]) : null;
	}

	private String languageOf(String language) {
		if (language == null) return null;
		return Locale.forLanguageTag(language).toString().replaceAll("_.*", "");
	}

	private boolean isMultipart() throws IOException, ServletException {
		return request.contentType() != null && request.contentType().contains("multipart/form-data");
	}

	private boolean isMultipart(String method) throws IOException, ServletException {
		return isMultipart() && request.raw().getPart(method) != null;
	}

	private Object readPart(String method) throws IOException, ServletException {
		Part part = request.raw().getPart(method);
		if (part.getContentType() == null)
			return readString(part.getInputStream());
		return part.getInputStream();
	}

	private String readString(InputStream stream) throws IOException {
		Scanner scanner = new Scanner(stream, "UTF-8").useDelimiter("\\A");
		return scanner.hasNext() ? scanner.next() : "";
	}
}
