package io.intino.alexandria.http.server;

import io.intino.alexandria.Context;

public class AlexandriaHttpContext extends Context {
	private final AlexandriaHttpManager<?> manager;

	public AlexandriaHttpContext(AlexandriaHttpManager<?> manager) {
		this.manager = manager;
		initialize();
	}

	public String domain() {
		return manager.domain();
	}

	public String baseUrl() {
		return manager.baseUrl();
	}

	public String requestUrl() {
		return manager.baseUrl() + manager.request().raw().getPathInfo();
	}

	public String realIp() {
		return manager.realIp();
	}

	public String queryParameter(String name) {
		return manager.fromQuery(name);
	}

	public String header(String name) {
		return (String) manager.fromHeader(name);
	}

	public void header(String name, String value) {
		manager.response().header(name, value);
	}

	public AlexandriaHttpManager<?> manager() {
		return manager;
	}

	private void initialize() {
		put("domain", domain());
		put("baseUrl", baseUrl());
		put("requestUrl", requestUrl());
		put("realIp", realIp());
	}
}
