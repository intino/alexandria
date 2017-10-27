package io.intino.konos.server.activity.displays.elements.model;

import java.time.Instant;

public class Item {
	private String id;
	private String name;
	private Object object;
	private Instant created;

	public String id() {
		return name;
	}

	public Item id(String id) {
		this.id = id;
		return this;
	}

	public String name() {
		return name;
	}

	public Item name(String name) {
		this.name = name;
		return this;
	}

	public <T> T object() {
		return (T) object;
	}

	public Item object(Object value) {
		this.object = value;
		return this;
	}

	public Instant created() {
		return created;
	}

	public Item created(Instant created) {
		this.created = created;
		return this;
	}
}
