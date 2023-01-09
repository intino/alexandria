package io.intino.alexandria.proxy;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import spark.Request;
import spark.Response;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Proxy {
	private URL localUrl;
	private URL remoteUrl;
	private ProxyAdapter adapter = null;

	public Proxy(URL localUrl, URL remoteUrl) {
		this.localUrl = localUrl;
		this.remoteUrl = remoteUrl;
	}

	public Proxy adapter(ProxyAdapter adapter) {
		this.adapter = adapter;
		return this;
	}

	public void get(Request request, Response response) throws Network.NetworkException, URISyntaxException {
		Network network = new Network();

		String query = request.queryString();
		if (query == null) query = ""; else query = "?" + query;

		String uri = pathOf(request);
		String url = remoteUrl + uri + query;

		network.setAdditionalHeaders(new ArrayList<>(request.headers().stream().map(h -> headerOf(h, request.headers(h))).collect(Collectors.toList())));
		byte[] content = network.sendGetString(url);
		fixHeaders(network, response);

		content = adaptText(network, request, remoteUrl, uri, content);
		writeResponse(response, content);
	}

	private byte[] adaptText(Network network, Request request, URL remoteUrl, String uri, byte[] content) {
		if (!isText(network)) return content;

		String textContent = new String(content, StandardCharsets.UTF_8);

		if (isHtml(network)) {
			if (!request.uri().contains("iframe.html")) textContent = adaptUrls(textContent, localUrl, remoteUrl, "src");
			textContent = adaptUrls(textContent, localUrl, remoteUrl, "href");
		}

		if (isCss(network)) {
			String subUri = uri.length() > 1 ? uri.substring(1) : uri;
			Path path = Paths.get(uri.substring(0, subUri.lastIndexOf("/") + 1)).getParent();
			textContent = textContent.replaceAll("url\\(\\.\\./", "url(" + localUrl + path.toString().replaceAll("\\\\", "/") + "/");
		}

		return adaptContent(textContent).getBytes();
	}

	private String adaptUrls(String content, URL localUrl, URL remoteUrl, String pattern) {
		content = content.replaceAll(" " + pattern + "=\"\\/\\/", " " + pattern + "_keep=\"//");
		content = content.replaceAll(" " + pattern + "=\"" + remoteUrl, " " + pattern + "_keep=\"" + localUrl);
		content = content.replaceAll(" " + pattern + "=\"/", " " + pattern + "=\"" + localUrl + "/");
		content = content.replaceAll(" " + pattern + "=\"./", " " + pattern + "=\"" + localUrl + "/./");
		content = content.replaceAll(" " + pattern + "_keep=", " " + pattern + "=");
		return content;
	}

	private boolean isText(Network network) {
		return isHtml(network) || isCss(network);
	}

	private boolean isHtml(Network network) {
		String contentType = contentType(network);
		return contentType != null && contentType.contains("text/html");
	}

	private boolean isCss(Network network) {
		String contentType = contentType(network);
		return contentType != null && contentType.contains("text/css");
	}

	private boolean isJs(Network network) {
		String contentType = contentType(network);
		return contentType != null && (contentType.contains("text/javascript") || contentType.contains("application/javascript"));
	}

	public void post(Request request, Response response) throws Network.NetworkException {
		Network network = new Network();

		String query = request.queryString();
		if (query == null) query = ""; else query = "?" + query;

		String url = remoteUrl + pathOf(request) + query;

		StringBuilder params = new StringBuilder();
		for (String key : request.queryParams()) {
			String value = request.queryParams(key);
			params.append("&").append(key).append("=").append(adaptParameter(key, value));
		}
		if (params.length() > 0) params = new StringBuilder(params.substring(1));

		network.setAdditionalHeaders(new ArrayList<>(request.headers().stream().map(h -> headerOf(h, request.headers(h))).collect(Collectors.toList())));
		byte[] content = network.sendPostString(url, params.toString());

		fixHeaders(network, response);

		content = adaptText(network, request, remoteUrl, pathOf(request), content);
		writeResponse(response, content);
	}

	private NameValuePair headerOf(String header, String value) {
		return new BasicNameValuePair(header, value);
	}

	private void fixHeaders(Network network, Response response) {
		response.removeCookie("JSESSIONID");
		Header[] headers = network.getLastHeaders();
		if (headers == null) return;
		for (Header header : headers) {
			if ("content-length".equalsIgnoreCase(header.getName()) ||
			    "Set-Cookie".equalsIgnoreCase(header.getName())) continue;

			response.header(header.getName(), header.getValue());
		}
	}

	private String contentType(Network network) {
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
		String uri = format(request.uri());
		String path = removeAddressPath(request, localUrl.getPath());
		uri = uri.replace(path, "");
		uri = uri.replace(path.substring(0, path.lastIndexOf("/")), "");
//		if ((uri.length() >= localUrl.getPath().length()) && uri.substring(0, localUrl.getPath().length()).equals(localUrl.getPath())) uri = uri.substring(localUrl.getPath().length());
		return uri;
	}

	private String format(String uri) {
		return uri.startsWith("//") ? uri.substring(1) : uri;
	}

	private String removeAddressPath(Request request, String path) {
		String header = request.raw().getHeader("X-Forwarded-Path");
		if (header == null) return path;
		return path.replace(header, "");
	}

	private String adaptParameter(String key, String value) {
		if (adapter == null) return value;
		return adapter.adaptParameter(localUrl, remoteUrl, key, value);
	}

	private String adaptContent(String content) {
		if (adapter == null) return content;
		return adapter.adaptContent(content);
	}

	private void writeResponse(Response response, byte[] content) throws Network.NetworkException {
		try {
			ServletOutputStream stream = response.raw().getOutputStream();
			response.raw().setContentLength(content.length);
			stream.write(content);
			stream.close();

//			Logger.debug(url + " - Out header - " + response.raw().getHeader("Content-Type"));

		} catch (IOException e) {
			throw new Network.NetworkException(e);
		}
	}
}
