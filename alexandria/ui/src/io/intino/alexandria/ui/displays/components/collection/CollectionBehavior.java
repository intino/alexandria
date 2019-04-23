package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.ui.model.Datasource;

public class CollectionBehavior<ItemComponent, Item> {
	private final Collection collection;
	private final ItemLoader<Item> itemLoader;

	public CollectionBehavior(Collection collection, Datasource source, int pageSize) {
		this.collection = collection;
		this.itemLoader = new ItemLoader<>(source, pageSize);
	}

	public void page(int pos) {
		collection.addAll(itemLoader.page(pos));
	}

	public void moreItems(CollectionMoreItems info) {
		java.util.List<Item> items = itemLoader.moreItems(info.start(), info.stop());
		for (int i=0; i<items.size(); i++) collection.insert(items.get(i), info.start()+i);
	}
}
