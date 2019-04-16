package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.displays.events.AddItemListener;

public interface Collection<ItemComponent, Item> {
	void onAddItem(AddItemListener listener);
}
