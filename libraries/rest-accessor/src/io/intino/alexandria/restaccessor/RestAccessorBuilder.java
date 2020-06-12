package io.intino.alexandria.restaccessor;

import io.intino.alexandria.Base64;
import io.intino.alexandria.Resource;
import io.intino.alexandria.restaccessor.RestAccessor.Response;
import io.intino.alexandria.restaccessor.exceptions.RestfulFailure;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RestAccessorBuilder {
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
	private final Map<String, String> entityParameters;
	private final List<Resource> resources;
	private int timeOutMillis;
	private Auth auth;

	public RestAccessorBuilder(URL url) {
		this.url = url;
		timeOutMillis = 120 * 1000;
		queryParameters = new ArrayList<>();
		headerParameters = new LinkedHashMap<>();
		entityParameters = new LinkedHashMap<>();
		resources = new ArrayList<>();
		this.client = client();
	}

	public RestAccessorBuilder timeOut(int timeOutMillis) {
		this.timeOutMillis = timeOutMillis;
		return this;
	}

	public RestAccessorBuilder basicAuth(String user, String password) {
		auth = Auth.Basic.with(Base64.encode((user + ":" + password).getBytes()));
		return this;
	}

	public RestAccessorBuilder bearerAuth(String token) {
		auth = Auth.Bearer.with(Base64.encode((token).getBytes()));
		return this;
	}

	public RestAccessorBuilder queryParameter(String name, String value) {
		queryParameters.add(new BasicNameValuePair(name, value));
		return this;
	}

	public RestAccessorBuilder headerParameter(String name, String value) {
		headerParameters.put(name, value);
		return this;
	}

	public RestAccessorBuilder entityParameter(String name, String value) {
		entityParameters.put(name, value);
		return this;
	}

	public RestAccessorBuilder entityResource(Resource resource) {
		resources.add(resource);
		return this;
	}

	public Response get(String path) throws RestfulFailure {
		return buildRequest(HttpGet.METHOD_NAME, path).execute();
	}

	public Response post(String path) throws RestfulFailure {
		return buildRequest(HttpPost.METHOD_NAME, path).execute();
	}

	public Response put(String path) throws RestfulFailure {
		return buildRequest(HttpPut.METHOD_NAME, path).execute();
	}

	public Response patch(String path) throws RestfulFailure {
		return buildRequest(HttpPatch.METHOD_NAME, path).execute();
	}

	public Response delete(String path) throws RestfulFailure {
		return buildRequest(HttpDelete.METHOD_NAME, path).execute();
	}

	private MethodExecutor buildRequest(String methodName, String path) throws RestfulFailure {
		try {
			HttpRequestBase method = method(methodName);
			method.setURI(new URIBuilder(pathUrl(url, path)).setParameters(queryParameters).build());
			if (auth != null) method.setHeader(HttpHeaders.AUTHORIZATION, auth.name() + " " + auth.token);
			headerParameters.forEach(method::setHeader);
			entityParameters.forEach((key, value) -> {

			});
			return () -> {
				try {
					return responseFrom(client.execute(method));
				} catch (IOException e) {
					throw new RestfulFailure(e.getMessage());
				}
			};

		} catch (URISyntaxException exception) {
			throw new RestfulFailure(exception.getMessage());
		}
	}

	private HttpClient client() {
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(this.timeOutMillis).build();
		return HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).setDefaultRequestConfig(requestConfig).build();
	}

	private HttpRequestBase method(String methodName) {
		if (methodName.equals(HttpGet.METHOD_NAME)) return new HttpGet();
		if (methodName.equals(HttpPost.METHOD_NAME)) return new HttpPost();
		if (methodName.equals(HttpPatch.METHOD_NAME)) return new HttpPatch();
		if (methodName.equals(HttpPut.METHOD_NAME)) return new HttpPut();
		return new HttpDelete();
	}

	private String pathUrl(URL url, String path) {
		String baseUrl = url.toString();
		if (baseUrl.endsWith("/")) baseUrl = baseUrl.substring(0, baseUrl.length() - 1);
		return path.isEmpty() ? baseUrl : baseUrl + (path.startsWith("/") ? path : "/" + path);
	}

	private Response responseFrom(HttpResponse response) {
		try {
			return new RestResponse(response.getStatusLine().getStatusCode(), response.getEntity().getContent());
		} catch (IOException e) {
			return new RestResponse(response.getStatusLine().getStatusCode(), null);
		}
	}

	interface MethodExecutor {
		Response execute() throws RestfulFailure;
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
				return IOUtils.toString(content, StandardCharsets.UTF_8);
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
