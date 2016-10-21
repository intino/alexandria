package io.intino.pandora.restful.core;

import io.intino.pandora.restful.RestfulApi;
import io.intino.pandora.restful.exceptions.RestfulFailure;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

public class RestfulAccessor implements RestfulApi {

	@Override
	public Response get(URL url, String path) throws RestfulFailure {
		return get(url, path, emptyMap());
	}

	@Override
	public Response get(URL url, String path, Map<String, String> parameters) throws RestfulFailure {
		return doGet(url, path, parametersToNameValuePairs(parameters));
	}

	@Override
	public Resource resourceFrom(URL url, String path) throws RestfulFailure {
		return doGetFile(url, path, emptyList());
	}

	@Override
	public Response post(URL url, String path) throws RestfulFailure {
		return post(url, path, emptyMap());
	}

	@Override
	public Response post(URL url, String path, Map<String, String> parameters) throws RestfulFailure {
		return doPost(url, path, entityOf(parameters));
	}

	@Override
	public Response post(URL url, String path, Resource resource) throws RestfulFailure {
		return doPost(url, path, multipartEntityOf(resource));
	}

	@Override
	public Response put(URL url, String path) throws RestfulFailure {
		return put(url, path, emptyMap());
	}

	@Override
	public Response put(URL url, String path, Map<String, String> parameters) throws RestfulFailure {
		return doPut(url, path, entityOf(parameters));
	}

	@Override
	public Response put(URL url, String path, Resource resource) throws RestfulFailure {
		return doPut(url, path, multipartEntityOf(resource));
	}

	@Override
	public Response delete(URL url, String path) throws RestfulFailure {
		return doDelete(url, path, emptyList());
	}

	@Override
	public Response delete(URL url, String path, Map<String, String> parameters) throws RestfulFailure {
		return doDelete(url, path, parametersToNameValuePairs(parameters));
	}

	@Override
	public RestfulSecureConnection secure(URL url, URL certificate, String password) {
		return new RestfulSecureConnection() {
			@Override
			public Response get(String path) throws RestfulFailure {
				return get(path, emptyMap());
			}

			@Override
			public Response get(String path, Map<String, String> parameters) throws RestfulFailure {
				List<NameValuePair> getParameters = parametersToNameValuePairs(parameters);
				getParameters.addAll(parametersToNameValuePairs(secureParameters(new HashMap<>(parameters), certificate, password)));
				return doGet(url, path, getParameters);
			}

			@Override
			public Resource getResource(String path) throws RestfulFailure {
				return doGetFile(url, path, parametersToNameValuePairs(secureParameters(emptyMap(), certificate, password)));
			}

			@Override
			public Response post(String path) throws RestfulFailure {
				return post(path, emptyMap());
			}

			@Override
			public Response post(String path, Map<String, String> parameters) throws RestfulFailure {
				return doPost(url, path, entityOf(parameters));
			}

			@Override
			public Response post(String path, Resource resource) throws RestfulFailure {
				return doPost(url, path, multipartEntityOf(resource));
			}

			@Override
			public Response put(String path) throws RestfulFailure {
				return put(path, emptyMap());
			}

			@Override
			public Response put(String path, Map<String, String> parameters) throws RestfulFailure {
				return doPut(url, path, entityOf(parameters));
			}

			@Override
			public Response delete(String path) throws RestfulFailure {
				return delete(path, emptyMap());
			}


			@Override
			public Response delete(String path, Map<String, String> parameters) throws RestfulFailure {
				List<NameValuePair> getParameters = parametersToNameValuePairs(parameters);
				getParameters.addAll(parametersToNameValuePairs(secureParameters(new HashMap<>(parameters), certificate, password)));
				return doDelete(url, path, getParameters);
			}

			private HttpEntity entityOf(Map<String, String> parameters) throws RestfulFailure {
				try {
					List<NameValuePair> entityParameters = parametersToNameValuePairs(parameters);
					entityParameters.addAll(parametersToNameValuePairs(secureParameters(new HashMap<>(parameters), certificate, password)));
					return new UrlEncodedFormEntity(entityParameters, "UTF-8");
				} catch (UnsupportedEncodingException exception) {
					throw new RestfulFailure(exception.getMessage());
				}
			}
		};
	}

	private String pathUrl(URL url, String path) {
		String baseUrl = url.toString();

		if (baseUrl.endsWith("/"))
			baseUrl = baseUrl.substring(0, baseUrl.length() - 1);

		return baseUrl + (path.startsWith("/") ? path : "/" + path);
	}

	private HttpClient client() {
		return HttpClientBuilder.create().build();
	}

	private Response doGet(URL url, String path, List<NameValuePair> parameters) throws RestfulFailure {
		try {
			URIBuilder uriBuilder = new URIBuilder(pathUrl(url, path)).setParameters(parameters);
			return executeMethod(url, new HttpGet(uriBuilder.build().toURL().toString()));
		} catch (URISyntaxException | MalformedURLException exception) {
			throw new RestfulFailure(exception.getMessage());
		}
	}

	private Resource doGetFile(URL url, String path, List<NameValuePair> parameters) throws RestfulFailure {
		try {
			URIBuilder uriBuilder = new URIBuilder(pathUrl(url, path)).setParameters(parameters);
			HttpResponse response;

			try {
				response = client().execute(new HttpGet(uriBuilder.build().toURL().toString()));
			} catch (IOException exception) {
				throw new RestfulFailure(exception.getMessage());
			}

			int status = response.getStatusLine().getStatusCode();
			if (status < 200 || status >= 300)
				throw new RestfulFailure(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));

			HttpEntity entity = response.getEntity();
			return new Resource(entity.getContent(), entity.getContentType().getValue());
		} catch (URISyntaxException | IOException exception) {
			throw new RestfulFailure(exception.getMessage());
		}
	}

	private Response doPost(URL url, String path, HttpEntity entity) throws RestfulFailure {
		HttpPost post = new HttpPost(pathUrl(url, path));
		post.setEntity(entity);
		return executeMethod(url, post);
	}

	private Response doPut(URL url, String path, HttpEntity entity) throws RestfulFailure {
		HttpPut put = new HttpPut(pathUrl(url, path));
		put.setEntity(entity);
		return executeMethod(url, put);
	}

	private Response doDelete(URL url, String path, List<NameValuePair> parameters) throws RestfulFailure {
		try {
			URIBuilder uriBuilder = new URIBuilder(pathUrl(url, path)).setParameters(parameters);
			return executeMethod(url, new HttpDelete(uriBuilder.build().toURL().toString()));
		} catch (URISyntaxException | MalformedURLException exception) {
			throw new RestfulFailure(exception.getMessage());
		}
	}

	private Response executeMethod(URL url, HttpRequestBase method) throws RestfulFailure {
		HttpResponse response;

		try {
			response = client().execute(method);
		} catch (IOException exception) {
			throw new RestfulFailure(exception.getMessage());
		}

		int status = response.getStatusLine().getStatusCode();
		if (status < 200 || status >= 300)
			throw new RestfulFailure(String.format("%s => %d - %s", url, status, response.getStatusLine().getReasonPhrase()));

		return responseOf(response);
	}

	private HttpEntity entityOf(Map<String, String> parameters) throws RestfulFailure {
		try {
			return new UrlEncodedFormEntity(parametersToNameValuePairs(parameters), "UTF-8");
		} catch (UnsupportedEncodingException exception) {
			throw new RestfulFailure(exception.getMessage());
		}
	}

	private HttpEntity multipartEntityOf(Resource resource) throws RestfulFailure {
		return multipartEntityOf(resource, null, null);
	}

	private HttpEntity multipartEntityOf(Resource resource, URL certificate, String password) throws RestfulFailure {
		MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();

		entityBuilder.setContentType(ContentType.MULTIPART_FORM_DATA);
		entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		entityBuilder.setCharset(Charset.forName("UTF-8"));

		addContent(entityBuilder, resource);
		addParameters(entityBuilder, resource);
		addSecureParameters(certificate, password, entityBuilder, resource);

		return entityBuilder.build();
	}

	private List<NameValuePair> parametersToNameValuePairs(Map<String, String> parameters) {
		List<NameValuePair> result = new ArrayList<>();
		parameters.forEach((name, value) -> result.add(new NameValuePair() {
			@Override
			public String getName() {
				return name;
			}

			@Override
			public String getValue() {
				return value;
			}
		}));
		return result;
	}

	private void addContent(MultipartEntityBuilder builder, Resource resource) throws RestfulFailure {
		builder.addPart("content", new InputStreamBody(resource.content(), resource.contentType()));
	}

	private void addParameters(MultipartEntityBuilder builder, Resource resource) throws RestfulFailure {
		resource.parameters().entrySet().forEach(param -> builder.addPart(param.getKey(), new StringBody(param.getValue(), ContentType.APPLICATION_JSON)));
	}

	private void addSecureParameters(URL certificate, String password, MultipartEntityBuilder entityBuilder, Resource resource) throws RestfulFailure {
		if (certificate == null)
			return;

		try {
			Map<String, String> secureParameters = secureParameters(parametersOf(resource), certificate, password);
			secureParameters.forEach((name, value) -> {
				entityBuilder.addTextBody(name, value, ContentType.TEXT_PLAIN);
			});
		} catch (Exception exception) {
			throw new RestfulFailure(String.format("Could not sign with certificate: %s", certificate.toString()));
		}
	}

	private Map<String, Object> parametersOf(Resource resource) {
		return new HashMap<String, Object>() {{
			put("contentType", resource.contentType());
			resource.parameters().entrySet().forEach(entry -> put(entry.getKey(), entry.getValue()));
		}};
	}

	private Map<String, String> secureParameters(Map<String, Object> parameters, URL certificate, String password) throws RestfulFailure {
		if (certificate == null)
			return emptyMap();

		try {
			Signer signer = new Signer();
			long timestamp = new Date().getTime();
			String hash = signer.hash(parameters, timestamp);

			Map<String, String> result = new HashMap<>();
			result.put("timestamp", String.valueOf(timestamp));
			result.put("hash", hash);
			result.put("signature", signer.sign(hash, certificate, password));
			return result;
		} catch (Exception exception) {
			throw new RestfulFailure(String.format("Could not sign with certificate: %s", certificate.toString()));
		}
	}

	private Response responseOf(HttpResponse response) {
		return new Response() {

			@Override
			public String content() {
				try {
					if (response == null)
						return null;

					return stringContentOf(response.getEntity().getContent());
				} catch (IOException e) {
					return null;
				}
			}

			private String stringContentOf(InputStream input) {
				BufferedReader buffer = null;
				StringBuilder sb = new StringBuilder();
				String line;

				try {
					buffer = new BufferedReader(new InputStreamReader(input));
					while ((line = buffer.readLine()) != null) sb.append(line);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (buffer != null) {
						try {
							buffer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}

				return sb.toString();
			}
		};
	}

}
