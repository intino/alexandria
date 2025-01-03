package io.intino.alexandria.ui.server.pages;

import io.intino.alexandria.ui.services.push.Browser;

public class Unit {
	public String name;
	public String url;
	public String socketPath;

	public Unit(String name, String url) {
		this(name, url, Browser.PushPath);
	}

	public Unit(String name, String url, String socketPath) {
		this.name = name;
		this.url = url;
		this.socketPath = socketPath;
	}

	public String name() {
		return name;
	}

	public Unit name(String name) {
		this.name = name;
		return this;
	}

	public String url() {
		return url;
	}

	public Unit url(String url) {
		this.url = url;
		return this;
	}

	public String socketPath() {
		return socketPath;
	}

	public Unit socketPath(String pushPath) {
		this.socketPath = pushPath;
		return this;
	}
}
