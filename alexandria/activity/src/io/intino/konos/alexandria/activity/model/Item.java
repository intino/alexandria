package io.intino.konos.alexandria.activity.model;

import java.time.Instant;

public class Item {
	private String id;
	private String name;
	private String label;
	private Object object;
	private Instant created;

	public String id() {
		return id;
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

	public String label() {
		return label;
	}

	public Item label(String label) {
		this.label = label;
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

	public static Item createFrom(Item item) {
		return new Item().id(item.id).name(item.name).label(item.label).object(item.object).created(item.created);
	}
}
