package io.intino.alexandria.framework.box.model.catalog.arrangement;

import java.util.List;

public class Group {
	private String name;
	private String label;
	private List<Object> objects;

	public String name() {
		return name;
	}

	public Group name(String name) {
		this.name = name;
		return this;
	}

	public String label() {
		return label;
	}

	public Group label(String label) {
		this.label = label;
		return this;
	}

	public List<Object> objects() {
		return this.objects;
	}

	public Group objects(List<Object> objects) {
		this.objects = objects;
		return this;
	}

	public int countItems() {
		return objects != null ? objects.size() : 0;
	}
}
