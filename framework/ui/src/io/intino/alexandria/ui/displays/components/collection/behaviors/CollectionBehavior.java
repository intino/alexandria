package io.intino.alexandria.ui.displays.components.collection.behaviors;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.loaders.ItemLoader;
import io.intino.alexandria.ui.model.Datasource;

import java.util.List;

public abstract class CollectionBehavior<DS extends Datasource<Item>, Item, IL extends ItemLoader<DS, Item>> {
	private final Collection collection;
	IL itemLoader;

	public CollectionBehavior(Collection collection) {
		this.collection = collection;
	}

	public <C extends Collection> C collection() {
		return (C) collection;
	}

    public IL itemLoader() {
		return itemLoader;
	}

	public CollectionBehavior setup(DS source) {
		if (source == null) return this;
		this.itemLoader = (IL) new ItemLoader(source);
		return this;
	}

	public void reload() {
		this.itemLoader.reload();
		reset();
	}

	public void filter(String grouping, List<String> groups) {
		itemLoader.filter(grouping, groups);
		reset();
	}

	public void condition(String condition) {
		itemLoader.condition(condition);
		reset();
	}

	public void timetag(Timetag timetag) {
		itemLoader.timetag(timetag);
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

	void reset() {
		collection.clear();
		update();
	}

	abstract void update();

}
