package io.intino.alexandria.proxy;

import org.apache.http.Header;
import spark.Request;
import spark.Response;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Proxy {
	private URL localUrl;
	private URL remoteUrl;
	private Network network = new Network();
	private ProxyAdapter adapter = null;

	public Proxy(URL localUrl, URL remoteUrl) {
		this.localUrl = localUrl;
		this.remoteUrl = remoteUrl;
	}

	public Proxy adapter(ProxyAdapter adapter) {
		this.adapter = adapter;
		return this;
	}

	public void get(Request request, Response response) throws Network.NetworkException {
		String query = request.queryString();
		if (query == null) query = ""; else query = "?" + query;

		String uri = pathOf(request);
		String url = remoteUrl + uri + query;

		byte[] content = network.sendGetString(url);
		fixHeaders(response);

		if (isText()) {
			String textContent = new String(content, StandardCharsets.UTF_8);

			if (isHtml()) {
				if (!request.uri().contains("iframe.html"))
					textContent = textContent.replaceAll(" src=\"", " src=\"" + localUrl + "/");
				textContent = textContent.replaceAll(" href=\"", " href=\"" + localUrl + "/");
			}

			if (isCss()) {
				String subUri = uri.length() > 1 ? uri.substring(1) : uri;
				textContent = textContent.replaceAll("url\\(\\.\\./", "url(" + localUrl + uri.substring(0, subUri.indexOf("/") + 1) + "/");
			}

			content = textContent.getBytes();
		}
		writeResponse(response, content);
	}

	private boolean isText() {
		return isHtml() || isCss();
	}

	private boolean isHtml() {
		String contentType = contentType();
		return contentType != null && contentType.contains("text/html");
	}

	private boolean isCss() {
		String contentType = contentType();
		return contentType != null && contentType.contains("text/css");
	}

	public void post(Request request, Response response) throws Network.NetworkException {
		String query = request.queryString();
		if (query == null) query = ""; else query = "?" + query;

		String url = remoteUrl + pathOf(request) + query;

		StringBuilder params = new StringBuilder();
		for (String key : request.queryParams()) {
			String value = request.queryParams(key);
			params.append("&").append(key).append("=").append(adaptParameter(key, value));
		}
		if (params.length() > 0) params = new StringBuilder(params.substring(1));

		byte[] content = network.sendPostString(url, params.toString());

		fixHeaders(response);
		writeResponse(response, content);
	}

	private void fixHeaders(Response response) {
		response.removeCookie("JSESSIONID");
		Header[] headers = network.getLastHeaders();
		if (headers == null) return;
		for (Header header : headers) {
			if ("content-length".equalsIgnoreCase(header.getName()) ||
			    "Set-Cookie".equalsIgnoreCase(header.getName())) continue;
			
			response.header(header.getName(), header.getValue());
		}
	}

	private String contentType() {
		Header[] headers = network.getLastHeaders();
		if (headers == null) return null;
		for (Header header : headers) {
			if ("content-type".equalsIgnoreCase(header.getName())) {
				return header.getValue();
			}
		}
		return null;
	}

	private String pathOf(Request request) {
		String uri = request.uri();
		String path = localUrl.getPath();
		uri = uri.replace(path, "");
		uri = uri.replace(path.substring(0, path.lastIndexOf("/")), "");
//		if ((uri.length() >= localUrl.getPath().length()) && uri.substring(0, localUrl.getPath().length()).equals(localUrl.getPath())) uri = uri.substring(localUrl.getPath().length());
		return uri;
	}

	private String adaptParameter(String key, String value) {
		if (adapter == null) return value;
		return adapter.adaptParameter(localUrl, remoteUrl, key, value);
	}

	private void writeResponse(Response response, byte[] content) throws Network.NetworkException {
		try {
			ServletOutputStream stream = response.raw().getOutputStream();
			response.raw().setContentLength(content.length);
			stream.write(content);
			stream.close();
		} catch (IOException e) {
			throw new Network.NetworkException(e);
		}
	}
}
