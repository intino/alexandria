package io.intino.alexandria.http.server;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

public interface AlexandriaHttpRequest {
	String sessionId();
	String uri();
	String header(String name);
	Map<String, String> headers();
	Map<String, String> cookies();
	String cookie(String name);
	List<String> queryParams();
	List<String> queryParams(String key);
	String queryParam(String key);
	String queryString();
	String pathParam(String name);
	String body();
	byte[] bodyAsBytes();

	String ip();
	HttpServletRequest raw();
}
