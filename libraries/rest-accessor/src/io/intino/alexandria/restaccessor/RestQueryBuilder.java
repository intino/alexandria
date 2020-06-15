package io.intino.alexandria.restaccessor;

import io.intino.alexandria.Base64;
import io.intino.alexandria.Json;
import io.intino.alexandria.Resource;
import io.intino.alexandria.exceptions.AlexandriaException;
import io.intino.alexandria.exceptions.ExceptionFactory;
import io.intino.alexandria.exceptions.InternalServerError;
import io.intino.alexandria.restaccessor.adapters.RequestAdapter;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.http.entity.ContentType.APPLICATION_OCTET_STREAM;
import static org.apache.http.entity.ContentType.MULTIPART_FORM_DATA;
import static org.apache.http.entity.mime.HttpMultipartMode.BROWSER_COMPATIBLE;

public class RestQueryBuilder {
	private enum Auth {
		Basic, Bearer;
		private String token;

		Auth with(String token) {
			this.token = token;
			return this;
		}

		public String token() {
			return token;
		}
	}

	private final URL url;
	private final List<NameValuePair> queryParameters;
	private final HttpClient client;
	private final Map<String, String> headerParameters;
	private final Map<String, String> entityParts;
	private final List<Resource> resources;
	private int timeOutMillis;
	private Auth auth;

	public RestQueryBuilder(URL url) {
		this.url = url;
		timeOutMillis = 120 * 1000;
		queryParameters = new ArrayList<>();
		headerParameters = new LinkedHashMap<>();
		entityParts = new LinkedHashMap<>();
		resources = new ArrayList<>();
		this.client = client();
	}

	public RestQueryBuilder timeOut(int timeOutMillis) {
		this.timeOutMillis = timeOutMillis;
		return this;
	}

	public RestQueryBuilder basicAuth(String user, String password) {
		auth = Auth.Basic.with(Base64.encode((user + ":" + password).getBytes()));
		return this;
	}

	public RestQueryBuilder bearerAuth(String token) {
		auth = Auth.Bearer.with(Base64.encode((token).getBytes()));
		return this;
	}

	public RestQueryBuilder queryParameter(String name, Object value) {
		if (value != null) queryParameters.add(new BasicNameValuePair(name, RequestAdapter.adapt(value)));
		return this;
	}

	public RestQueryBuilder headerParameter(String name, Object value) {
		if (value != null) headerParameters.put(name, RequestAdapter.adapt(value));
		return this;
	}

	public RestQueryBuilder entityPart(String name, Object content) {
		if (content != null) entityParts.put(name, RequestAdapter.adapt(content));
		return this;
	}

	public RestQueryBuilder entityPart(Resource resource) {
		resources.add(resource);
		return this;
	}

	public Response get(String path) throws AlexandriaException {
		return buildRequest(HttpGet.METHOD_NAME, path).execute();
	}

	public Response post(String path) throws AlexandriaException {
		return buildRequest(HttpPost.METHOD_NAME, path).execute();
	}

	public Response put(String path) throws AlexandriaException {
		return buildRequest(HttpPut.METHOD_NAME, path).execute();
	}

	public Response patch(String path) throws AlexandriaException {
		return buildRequest(HttpPatch.METHOD_NAME, path).execute();
	}

	public Response delete(String path) throws AlexandriaException {
		return buildRequest(HttpDelete.METHOD_NAME, path).execute();
	}

	private MethodExecutor buildRequest(String methodName, String path) throws AlexandriaException {
		try {
			HttpRequestBase method = method(methodName);
			method.setURI(new URIBuilder(pathUrl(url, path)).setParameters(queryParameters).build());
			if (auth != null) method.setHeader(HttpHeaders.AUTHORIZATION, auth.name() + " " + auth.token);
			headerParameters.forEach(method::setHeader);
			if (!entityParts.isEmpty() && method instanceof HttpEntityEnclosingRequestBase)
				((HttpEntityEnclosingRequestBase) method).setEntity(buildEntity());
			return () -> {
				try {
					return responseFrom(client.execute(method));
				} catch (IOException e) {
					throw new InternalServerError(e.getMessage());
				}
			};
		} catch (URISyntaxException exception) {
			throw new InternalServerError(exception.getMessage());
		}
	}

	private HttpEntity buildEntity() {
		if (resources.isEmpty() && entityParts.size() == 1)
			return stringEntity(String.valueOf(entityParts.values().iterator().next()));
		return multipartEntity();
	}

	private HttpEntity multipartEntity() {
		MultipartEntityBuilder builder = MultipartEntityBuilder.create().setContentType(MULTIPART_FORM_DATA).setMode(BROWSER_COMPATIBLE).setCharset(UTF_8);
		resources.forEach(r -> builder.addPart(r.name(), new InputStreamBody(r.stream(), r.type() != null ? ContentType.create(r.type()) : APPLICATION_OCTET_STREAM, r.name())));
		entityParts.forEach((key, value) -> builder.addPart(key, new StringBody(value, ContentType.APPLICATION_JSON)));
		return builder.build();
	}

	private HttpEntity stringEntity(String body) {
		StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
		entity.setContentEncoding(UTF_8.displayName());
		return entity;
	}

	private HttpClient client() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(this.timeOutMillis).build();
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

	private Response responseFrom(HttpResponse response) throws AlexandriaException {
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode < 200 || statusCode >= 300) {
				String bodyContent = getBodyContent(response);
				throw exception(statusCode, bodyContent);
			}
			return new RestResponse(statusCode, response.getEntity().getContent());
		} catch (IOException e) {
			return new RestResponse(response.getStatusLine().getStatusCode(), null);
		}
	}

	private AlexandriaException exception(int statusCode, String bodyContent) {
		AlexandriaException e = bodyContent.startsWith("{") ? Json.fromString(bodyContent, AlexandriaException.class) : null;
		if (e != null) return ExceptionFactory.from(statusCode, e.getMessage(), e.parameters());
		return ExceptionFactory.from(statusCode, bodyContent, Map.of());
	}

	private String getBodyContent(HttpResponse response) {
		try {
			InputStream content = response.getEntity().getContent();
			return IOUtils.toString(content, UTF_8);
		} catch (IOException e) {
			return "";
		}
	}

	interface MethodExecutor {
		Response execute() throws AlexandriaException;
	}

	public static class RestResponse implements Response {
		private final int code;
		private final InputStream content;

		public RestResponse(int code, InputStream content) {
			this.code = code;
			this.content = content;
		}

		public int code() {
			return code;
		}

		@Override
		public String content() {
			try {
				return IOUtils.toString(content, UTF_8);
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