package io.intino.alexandria.rest.spark;

import io.intino.alexandria.Context;

public class SparkContext extends Context {
	private final SparkManager manager;

	public SparkContext(SparkManager manager) {
		this.manager = manager;
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
		return (String) manager.fromQuery(name, String.class);
	}

	public String header(String name) {
		return (String) manager.fromHeader(name, String.class);
	}
}
