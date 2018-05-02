package io.intino.konos.alexandria.ui.model.view.container;

import io.intino.konos.alexandria.ui.model.view.set.AbstractItem;

import java.util.ArrayList;
import java.util.List;

public class SetContainer extends Container {
	private List<AbstractItem> items = new ArrayList<>();

	public List<AbstractItem> items() {
		return items;
	}

	public SetContainer add(AbstractItem item) {
		items.add(item);
		return this;
	}
}
