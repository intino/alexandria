package io.intino.alexandria.http.javalin;

import io.intino.alexandria.http.server.AlexandriaHttpRequest;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavalinHttpRequest implements AlexandriaHttpRequest {
	private final Context context;

	public JavalinHttpRequest(Context context) {
		this.context = context;
	}

	@Override
	public String sessionId() {
		return context.req().getSession().getId();
	}

	@Override
	public String uri() {
		return context.req().getRequestURI();
	}

	@Override
	public String header(String name) {
		return context.header(name);
	}

	@Override
	public Map<String, String> headers() {
		return context.headerMap();
	}

	@Override
	public Map<String, String> cookies() {
		return context.cookieMap();
	}

	@Override
	public String cookie(String name) {
		return context.cookie(name);
	}

	@Override
	public List<String> queryParams() {
		return new ArrayList<>(context.queryParamMap().keySet());
	}

	@Override
	public List<String> queryParams(String key) {
		return context.queryParams(key);
	}

	@Override
	public String queryParam(String key) {
		return context.queryParam(key);
	}

	@Override
	public String queryString() {
		return context.queryString();
	}

	@Override
	public String pathParam(String name) {
		return context.pathParamMap().getOrDefault(name, null);
	}

	@Override
	public String body() {
		return context.body();
	}

	@Override
	public byte[] bodyAsBytes() {
		return context.bodyAsBytes();
	}

	@Override
	public String ip() {
		return context.ip();
	}

	@Override
	public HttpServletRequest raw() {
		return context.req();
	}

}
