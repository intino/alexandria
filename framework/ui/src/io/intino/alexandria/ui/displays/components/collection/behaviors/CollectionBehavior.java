package io.intino.alexandria.ui.displays.components.collection.behaviors;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.loaders.ItemLoader;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Filter;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

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
		computeUpdate(e -> this.itemLoader.reload());
	}

	public java.util.List<Filter> filters() {
		return itemLoader.filters();
	}

	public void filters(java.util.List<Filter> filters) {
		itemLoader.filters(filters);
	}

	public void clearFilters() {
		computeUpdate(e -> itemLoader.clearFilters());
	}

	public void filter(String grouping, List<String> groups) {
		computeUpdate(e -> itemLoader.filter(grouping, groups));
	}

	public void filter(Map<String, List<String>> groupings) {
		computeUpdate(e -> itemLoader.filter(groupings));
	}

	public void filter(String grouping, Instant from, Instant to) {
		computeUpdate(e -> itemLoader.filter(grouping, from, to));
	}

	public void removeFilter(String grouping) {
		computeUpdate(e -> itemLoader.removeFilter(grouping));
	}

	public String condition() {
		return itemLoader.condition();
	}

	public void condition(String condition) {
		computeUpdate(e -> itemLoader.condition(condition));
	}

	public void timetag(Timetag timetag) {
		computeUpdate(e -> itemLoader.timetag(timetag));
	}

	public List<String> sortings() {
		return itemLoader.sortings();
	}

	public void sortings(List<String> sortings) {
		computeUpdate(e -> itemLoader.sortings(sortings));
	}

	public void addSorting(String sorting) {
		computeUpdate(e -> itemLoader.addSorting(sorting));
	}

	public void removeSorting(String sorting) {
		computeUpdate(e -> itemLoader.removeSorting(sorting));
	}

	public List<Item> items(String... sortings) {
		return itemLoader.items(sortings);
	}

	public long itemCount() {
		return itemLoader.itemCount();
	}

	void computeUpdate(Consumer<Void> consumer) {
		computeUpdate(consumer, true);
	}

	void computeUpdate(Consumer<Void> consumer, boolean reset) {
		collection.loading(true);
		consumer.accept(null);
		if (reset) reset();
		collection.loading(false);
	}

	void reset() {
		collection.clear();
		update();
	}

	abstract void update();

}
