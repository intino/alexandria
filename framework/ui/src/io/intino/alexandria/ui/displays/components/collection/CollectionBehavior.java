package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.model.Datasource;

import java.util.List;

public abstract class CollectionBehavior<DS extends Datasource<Item>, Item> {
	final Collection collection;
	ItemLoader<DS, Item> itemLoader;

	public CollectionBehavior(Collection collection) {
		this.collection = collection;
	}

    public <IL extends ItemLoader<DS, Item>> IL itemLoader() {
		return (IL) itemLoader;
	}

	public CollectionBehavior itemLoader(ItemLoader<DS, Item> loader) {
		this.itemLoader = loader;
		return this;
	}

	public void filter(String grouping, List<String> groups) {
		itemLoader.filter(grouping, groups);
		reset();
	}

	public void condition(String condition) {
		itemLoader.condition(condition);
		reset();
	}

	public void sortings(List<String> sortings) {
		itemLoader.sortings(sortings);
		reset();
	}

	public void addSorting(String sorting) {
		itemLoader.addSorting(sorting);
		reset();
	}

	public void removeSorting(String sorting) {
		itemLoader.removeSorting(sorting);
		reset();
	}

	public long itemCount() {
		return itemLoader.itemCount();
	}

	abstract void reset();

	void reset(List<Item> items) {
		collection.clear();
		collection.add(items);
	}

}
