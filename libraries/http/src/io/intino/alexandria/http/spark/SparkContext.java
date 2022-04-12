package io.intino.alexandria.http.spark;

import io.intino.alexandria.Context;

public class SparkContext extends Context {
	private final SparkManager manager;

	public SparkContext(SparkManager manager) {
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
		return manager.baseUrl() + manager.request().pathInfo();
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
		manager.response.raw().setHeader(name, value);
	}

	public SparkManager manager() {
		return manager;
	}

	private void initialize() {
		put("domain", domain());
		put("baseUrl", baseUrl());
		put("requestUrl", requestUrl());
		put("realIp", realIp());
	}
}
