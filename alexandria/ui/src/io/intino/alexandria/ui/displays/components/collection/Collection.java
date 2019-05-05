package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.displays.events.AddItemListener;

public interface Collection<ItemComponent, Item> {
	ItemComponent add(Item item);
	ItemComponent insert(Item item, int index);
	void onAddItem(AddItemListener listener);
	void clear();

	default void addAll(java.util.List<Item> items) {
		items.forEach(this::add);
	}
}
