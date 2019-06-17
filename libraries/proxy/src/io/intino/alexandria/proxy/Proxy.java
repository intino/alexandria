package io.intino.alexandria.proxy;

import org.apache.http.Header;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

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

		String content = network.sendGetString(url);

		if (! request.uri().contains("iframe.html"))
			content = content.replaceAll(" src=\"", " src=\""+localUrl+"/");
		content = content.replaceAll(" href=\"", " href=\""+localUrl+"/");

		addHeaders(response);
		writeResponse(response, content);
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

		String content = network.sendPostString(url, params.toString());

		addHeaders(response);
		writeResponse(response, content);
	}

	private void addHeaders(Response response) {
		Header[] headers = network.getLastHeaders();
		if (headers == null) return;
		for (Header header : headers) {
			if ("content-type".equals(header.getName())) {
				response.header(header.getName(), header.getValue());
			}
		}
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

	private void writeResponse(Response response, String content) throws Network.NetworkException {
		try {
			PrintWriter writer = response.raw().getWriter();
			writer.print(content);
			writer.close();
		} catch (IOException e) {
			throw new Network.NetworkException(e);
		}
	}
}
