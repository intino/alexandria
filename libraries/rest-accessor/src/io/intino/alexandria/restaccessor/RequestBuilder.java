package io.intino.alexandria.restaccessor;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import io.intino.alexandria.Base64;
import io.intino.alexandria.Json;
import io.intino.alexandria.Resource;
import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.exceptions.ExceptionFactory;
import io.intino.alexandria.exceptions.InternalServerError;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.restaccessor.adapters.RequestAdapter;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM;
import static org.apache.http.entity.ContentType.MULTIPART_FORM_DATA;
import static org.apache.http.entity.mime.HttpMultipartMode.BROWSER_COMPATIBLE;

public class RequestBuilder {
	public enum Method {
		GET, POST, PUT, PATCH, DELETE
	}

	private final URL url;
	private final HttpHost proxy;
	private final List<NameValuePair> queryParameters;
	private final Map<String, String> headerParameters;
	private final Map<String, String> entityParts;
	private UrlEncodedFormEntity urlEncodedFormEntity;
	private final List<Resource> resources;
	private int timeOutMillis;
	private Auth auth;
	private Method method;
	private String path;

	public RequestBuilder(URL url) {
		this(url, null);
	}

	public RequestBuilder(URL url, HttpHost proxy) {
		this.url = url;
		this.proxy = proxy;
		timeOutMillis = 120 * 1000;
		queryParameters = new ArrayList<>();
		headerParameters = new LinkedHashMap<>();
		entityParts = new LinkedHashMap<>();
		resources = new ArrayList<>();
	}

	public RequestBuilder timeOut(int timeOutMillis) {
		this.timeOutMillis = timeOutMillis;
		return this;
	}

	public RequestBuilder basicAuth(String user, String password) {
		auth = new Auth(Auth.Type.Basic, Base64.encode((user + ":" + password).getBytes()));
		return this;
	}

	public RequestBuilder bearerAuth(String token) {
		auth = new Auth(Auth.Type.Bearer, token);
		return this;
	}

	public RequestBuilder queryParameter(String name, Object value) {
		if (value != null) queryParameters.add(new BasicNameValuePair(name, RequestAdapter.adapt(value)));
		return this;
	}

	public RequestBuilder headerParameter(String name, Object value) {
		if (value != null) headerParameters.put(name, RequestAdapter.adapt(value));
		return this;
	}

	public RequestBuilder entityPart(String name, Object content) {
		if (content != null) entityParts.put(name, RequestAdapter.adapt(content));
		return this;
	}

	public RequestBuilder entityPart(Resource resource) {
		resources.add(resource);
		return this;
	}

	public RequestBuilder entityPart(List<Resource> resources) {
		this.resources.addAll(resources);
		return this;
	}

	public RequestBuilder entityPart(UrlEncodedFormEntity urlEncodedFormEntity) {
		if (urlEncodedFormEntity != null) this.urlEncodedFormEntity = urlEncodedFormEntity;
		return this;
	}


	public Request build(Method method, String path) {
		this.method = method;
		this.path = path;
		return build();
	}

	Request build() {
		return () -> {
			try {
				HttpRequestBase request = method(method.name());
				request.setURI(buildUrl());
				if (auth != null) request.setHeader(HttpHeaders.AUTHORIZATION, auth.type() + " " + auth.token);
				headerParameters.forEach(request::setHeader);
				if ((!resources.isEmpty() || !entityParts.isEmpty()) && request instanceof HttpEntityEnclosingRequestBase)
					((HttpEntityEnclosingRequestBase) request).setEntity(buildEntity());
				else if (urlEncodedFormEntity != null && request instanceof HttpEntityEnclosingRequestBase)
					((HttpEntityEnclosingRequestBase) request).setEntity(urlEncodedFormEntity);
				return parseResponse(client().execute(request));
			} catch (IOException | URISyntaxException e) {
				throw new InternalServerError(e.getMessage());
			}
		};
	}

	private URI buildUrl() throws URISyntaxException {
		return new URIBuilder(pathUrl(url, path)).setParameters(queryParameters).build();
	}

	private HttpEntity buildEntity() {
		if (resources.isEmpty() && entityParts.size() == 1)
			return stringEntity(String.valueOf(entityParts.values().iterator().next()));
		return multipartEntity();
	}

	private HttpEntity multipartEntity() {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create().setContentType(MULTIPART_FORM_DATA).setMode(BROWSER_COMPATIBLE).setCharset(UTF_8);
		resources.forEach(r -> builder.addPart(r.name(), new InputStreamBody(stream(r), r.type() != null ? ContentType.create(r.type()) : APPLICATION_OCTET_STREAM, r.name())));
		entityParts.forEach((key, value) -> builder.addPart(key, new StringBody(value, ContentType.APPLICATION_JSON)));
		return builder.build();
	}

	private static InputStream stream(Resource r) {
		try {
			return r.stream();
		} catch (IOException e) {
			Logger.error(e);
			return InputStream.nullInputStream();
		}
	}

	private HttpEntity stringEntity(String body) {
		StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
		entity.setContentEncoding(UTF_8.displayName());
		return entity;
	}

	private HttpClient client() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(this.timeOutMillis).setProxy(proxy).build();
		return HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).setDefaultRequestConfig(requestConfig).build();
	}

	private HttpRequestBase method(String methodName) {
		if (HttpGet.METHOD_NAME.equals(methodName)) return new HttpGet();
		if (HttpPost.METHOD_NAME.equals(methodName)) return new HttpPost();
		if (HttpPatch.METHOD_NAME.equals(methodName)) return new HttpPatch();
		if (HttpPut.METHOD_NAME.equals(methodName)) return new HttpPut();
		return new HttpDelete();
	}

	private String pathUrl(URL url, String path) {
		String baseUrl = url.toString();
		if (baseUrl.endsWith("/")) baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		return path.isEmpty() ? baseUrl : baseUrl + (path.startsWith("/") ? path : "/" + path);
	}

	private Response parseResponse(HttpResponse response) throws AlexandriaException {
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode < 200 || statusCode >= 300) throw exception(statusCode, bodyContent(response));
			return new RestResponse(statusCode, response.getAllHeaders(), response.getEntity().getContent());
		} catch (IOException e) {
			return new RestResponse(response.getStatusLine().getStatusCode(), response.getAllHeaders(), null);
		}
	}

	private AlexandriaException exception(int statusCode, String bodyContent) {
		try {
			AlexandriaException e = bodyContent.startsWith("{") ? alexandriaException(bodyContent) : null;
			if (e != null) return ExceptionFactory.from(statusCode, e.getMessage(), e.parameters());
			return ExceptionFactory.from(statusCode, bodyContent, Map.of());
		} catch (JsonSyntaxException e) {
			Logger.warn(e.getMessage() + ": " + bodyContent);
			return ExceptionFactory.from(statusCode, bodyContent, Map.of());
		}
	}

	private static AlexandriaException alexandriaException(String bodyContent) {
		JsonObject jsonObject = Json.fromString(bodyContent, JsonObject.class);
		Type asMap = new TypeToken<Map<String, String>>() {
		}.getType();
		return new AlexandriaException(jsonObject.get("code").getAsString(),
				jsonObject.get("detailMessage").getAsString(),
				jsonObject.has("parameters") ? Json.fromJson(jsonObject.get("parameters").getAsJsonObject(), asMap) : null);
	}

	private String bodyContent(HttpResponse response) {
		try {
			InputStream content = response.getEntity().getContent();
			String value = new String(content.readAllBytes(), UTF_8);
			content.close();
			return value;
		} catch (IOException e) {
			return "";
		}
	}

	public interface Request {
		Response execute() throws AlexandriaException;
	}

	private static class Auth {
		public enum Type {
			Basic, Bearer;
		}

		private final Type type;
		private final String token;

		public Auth(Type type, String token) {
			this.type = type;
			this.token = token;
		}

		public String token() {
			return token;
		}

		public Type type() {
			return type;
		}
	}

	public static class RestResponse implements Response {
		private final int code;
		private final InputStream content;
		private final Map<String, String> headers;

		public RestResponse(int code, Header[] headers, InputStream content) {
			this.code = code;
			this.headers = Arrays.stream(headers).collect(Collectors.toMap(NameValuePair::getName, NameValuePair::getValue, (v1, v2) -> v1));
			this.content = content;
		}

		public int code() {
			return code;
		}

		@Override
		public Map<String, String> headers() {
			return headers;
		}

		@Override
		public String contentType() {
			return headers.get("Content-Type");
		}

		@Override
		public String content() {
			try {
				String value = new String(content.readAllBytes(), UTF_8);
				content.close();
				return value;
			} catch (IOException e) {
				return null;
			}
		}


		@Override
		public InputStream contentAsStream() {
			return content;
		}
	}
}