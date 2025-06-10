package io.intino.alexandria.http.javalin;

import io.intino.alexandria.http.server.AlexandriaHttpRequest;
import io.javalin.http.Context;
import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

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
		Set<String> result = new HashSet<>(context.queryParamMap().keySet());
		result.addAll(context.formParamMap().keySet());
		result.addAll(context.req().getParameterMap().keySet());
		return new ArrayList<>(result);
	}

	@Override
	public List<String> queryParams(String key) {
		List<String> result = new ArrayList<>(context.queryParams(key));
		if (result.isEmpty()) result.addAll(context.formParams(key));
		if (result.isEmpty()) result.add(context.req().getParameter(key));
		return result;
	}

	@Override
	public String queryParam(String key) {
		String result = context.queryParam(key);
		result = result != null ? result : context.formParam(key);
		result = result != null ? result : context.req().getParameter(key);
		return result;
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
