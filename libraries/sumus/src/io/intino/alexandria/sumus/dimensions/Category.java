package io.intino.alexandria.sumus.dimensions;

public class Category {
	public final int id;
	public final String label;

	public Category(int id, String label) {
		this.id = id;
		this.label = label;
	}

	public Category parent() {
		int idx = label.lastIndexOf('.');
		return idx > 0 ? new Category(-1, label.substring(0,idx)) : null;
	}

	public boolean isParent() {
		return id == -1;
	}

	public boolean isChild() {
		return !isParent();
	}

	@Override
	public String toString() {
		return id + ":" + label;
	}
}
