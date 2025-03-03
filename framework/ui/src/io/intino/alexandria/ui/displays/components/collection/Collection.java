package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.displays.events.editable.AddCollectionItemListener;

import java.util.List;

public interface Collection<ItemComponent, Item> {
	ItemComponent add(Item item);
	List<ItemComponent> add(List<Item> items);
	ItemComponent insert(Item item, int index);
	List<ItemComponent> insert(List<Item> items, int from);
	ItemComponent create(Item item);
	void onAddItem(AddCollectionItemListener listener);
	void clear();
	void loading(boolean value);
}
