package io.intino.konos.alexandria.ui.model.catalog;

import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.catalog.arrangement.Group;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scope {
	private Map<String, List<Group>> groups = new HashMap<>();
	private Map<String, List<Object>> objects = new HashMap<>();
	private Item target = null;

	public Scope clear() {
		this.target = null;
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

	public Object target() {
		return target != null ? target.object() : null;
	}

	public Scope target(Item target) {
		this.target = target;
		return this;
	}
}
