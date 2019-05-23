package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Filter;

import java.util.*;

public class ItemLoader<DS extends Datasource<Item>, Item> {
	private long itemCount;
	final DS source;
	String condition;
	List<Filter> filters = new ArrayList<>();
	HashSet<String> sortings = new HashSet<>();

	public ItemLoader(DS source) {
		this.source = source;
		this.itemCount = source.itemCount();
	}

	public ItemLoader filter(String grouping, List<String> groups) {
		if (groups.size() <= 0) remove(grouping);
		else {
			Filter filter = filter(grouping);
			if (filter == null) filters.add(new Filter(grouping, groups));
			else filter.groups(groups);
		}
		this.itemCount = source.itemCount(condition, filters);
		return this;
	}

	public ItemLoader condition(String condition) {
		this.condition = condition;
		this.itemCount = source.itemCount(condition, filters);
		return this;
	}

	public void sortings(List<String> sortings) {
		this.sortings.clear();
		this.sortings.addAll(sortings);
	}

	public void addSorting(String sorting) {
		this.sortings.add(sorting);
	}

	public void removeSorting(String sorting) {
		this.sortings.remove(sorting);
	}

	public long itemCount() {
		return itemCount;
	}

	public List<Item> moreItems(int start, int stop) {
		return source.items(start, stop - start + 1, condition, filters, new ArrayList<>(sortings));
	}

	private Filter filter(String grouping) {
		return filters.stream().filter(f -> f.grouping().equalsIgnoreCase(grouping)).findFirst().orElse(null);
	}

	private void remove(String grouping) {
		Filter filter = filter(grouping);
		if (filter != null) filters.remove(filter);
	}
}