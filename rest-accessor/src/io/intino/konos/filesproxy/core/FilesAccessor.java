package io.intino.konos.filesproxy.core;

import cottons.utils.MimeTypes;
import io.intino.konos.Resource;
import io.intino.konos.filesproxy.FilesApi;
import io.intino.konos.filesproxy.exceptions.FilesApiFailure;
import io.intino.konos.restful.RestfulApi;
import io.intino.konos.restful.core.RestfulAccessor;
import io.intino.konos.restful.exceptions.RestfulFailure;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import static java.util.Collections.emptyMap;

public class FilesAccessor implements FilesApi {
	private final String basePath;
	private RestfulApi client;

	public FilesAccessor() {
		this("/file");
	}

	public FilesAccessor(String basePath) {
		this.basePath = normalize(basePath);
		this.client = new RestfulAccessor();
	}

	@Override
	public Connection connect(String url) {
		return connect(url, null, null);
	}

	@Override
	public Connection connect(String url, URL certificate, String password) {
		return new Connection() {
			private RestfulApi.RestfulSecureConnection connection = null;

			@Override
			public String upload(Resource resource) throws FilesApiFailure {
				return upload("", resource);
			}

			@Override
			public String upload(String path, Resource resource) throws FilesApiFailure {
				try {
					RestfulApi.Response response = secure().post(pathOf(path), resource);
					return response.content();
				} catch (RestfulFailure error) {
					throw new FilesApiFailure(error.getMessage());
				}
			}

			@Override
			public String upload(InputStream content, String contentType) throws FilesApiFailure {
				return upload(resourceOf(content, contentType, emptyMap()));
			}

			@Override
			public String upload(String path, InputStream content, String contentType) throws FilesApiFailure {
				return upload(path, resourceOf(content, contentType, emptyMap()));
			}

			@Override
			public String upload(InputStream content, String contentType, Map<String, String> parameters) throws FilesApiFailure {
				return upload(resourceOf(content, contentType, parameters));
			}

			@Override
			public String upload(String path, InputStream content, String contentType, Map<String, String> parameters) throws FilesApiFailure {
				return upload(path, resourceOf(content, contentType, parameters));
			}

			@Override
			public Resource download(String id) throws FilesApiFailure {
				return download("", id);
			}

			@Override
			public Resource download(String path, String id) throws FilesApiFailure {
				try {
					return secure().getResource(String.format("%s/%s", pathOf(path), id));
				} catch (RestfulFailure error) {
					throw new FilesApiFailure(error.getMessage());
				}
			}

			private RestfulApi.RestfulSecureConnection secure() throws FilesApiFailure {
				try {
					if (connection == null)
						connection = client.secure(new URL(url), certificate, password);
					return connection;
				} catch (MalformedURLException error) {
					throw new FilesApiFailure(error.getMessage());
				}
			}

			private Resource resourceOf(InputStream content, String contentType, Map<String, String> parameters) {
				Resource resource = new Resource("content", "resource." + MimeTypes.getExtension(contentType), contentType, content);
				parameters.entrySet().forEach(entry -> resource.addParameter(entry.getKey(), entry.getValue()));
				return resource;
			}

			private String pathOf(String path) {
				String result = basePath + (path.startsWith("/") ? path : "/" + path);
				if (result.endsWith("/")) result = result.substring(0, result.length() - 1);
				return result;
			}

		};
	}

	private String normalize(String basePath) {
		String path = basePath.endsWith("/") ? basePath.substring(0, basePath.length() - 1) : basePath;
		if (!path.startsWith("/")) path = "/" + path;
		return path;
	}

}
