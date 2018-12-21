package io.intino.alexandria.ui.model.catalog.arrangement;

import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.services.push.UISession;

public class Sorting extends Arrangement {
	private Comparator comparator;
	private boolean visible = true;

	public int compare(Item item1, Item item2, UISession session) {
		return comparator != null ? comparator.compare(item1.object(), item2.object(), session) : 0;
	}

	public Sorting comparator(Comparator comparator) {
		this.comparator = comparator;
		return this;
	}

	public boolean visible() {
		return visible;
	}

	public Sorting visible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public interface Comparator {
		int compare(Object item1, Object item2, UISession session);
	}

}
