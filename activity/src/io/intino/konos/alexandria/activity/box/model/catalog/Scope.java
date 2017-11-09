package io.intino.konos.alexandria.activity.box.model.catalog;

import io.intino.konos.alexandria.activity.box.model.catalog.arrangement.Group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scope {
	private Map<String, List<Group>> groups = new HashMap<>();
	private Map<String, List<Object>> objects = new HashMap<>();

	public Scope clear() {
		this.groups.clear();
		this.objects.clear();
		return this;
	}

	public Map<String, List<Group>> groups() {
		return this.groups;
	}

	public Scope groups(Map<String, List<Group>> groups) {
		this.groups.clear();
		this.groups.putAll(groups);
		return this;
	}

	public Map<String, List<Object>> objects() {
		return this.objects;
	}

	public Scope objects(Map<String, List<Object>> items) {
		this.objects.clear();
		this.objects.putAll(items);
		return this;
	}
}
