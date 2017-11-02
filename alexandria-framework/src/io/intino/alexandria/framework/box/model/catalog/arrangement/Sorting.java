package io.intino.alexandria.framework.box.model.catalog.arrangement;

import io.intino.alexandria.framework.box.model.Item;

public class Sorting extends Arrangement {
	private Comparator comparator;

	public int compare(Item item1, Item item2) {
		return comparator != null ? comparator.compare(item1.object(), item2.object()) : 0;
	}

	public Sorting comparator(Comparator comparator) {
		this.comparator = comparator;
		return this;
	}

	public interface Comparator {
		int compare(Object item1, Object item2);
	}

}
