package io.intino.alexandria.ui.displays.components.collection.loaders;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.TemporalDatasource;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ItemLoader<DS extends Datasource<Item>, Item> {
	private long itemCount;
	final DS source;
	String condition;
	List<Filter> filters = new ArrayList<>();
	HashSet<String> sortings = new HashSet<>();
	Timetag timetag = null;

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
		this.itemCount = calculateItemCount(condition);
		return this;
	}

	public ItemLoader condition(String condition) {
		this.condition = condition;
		this.itemCount = calculateItemCount(condition);
		return this;
	}

	public ItemLoader timetag(Timetag timetag) {
		this.timetag = timetag;
		this.itemCount = calculateItemCount(condition);
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

	private Filter filter(String grouping) {
		return filters.stream().filter(f -> f.grouping().equalsIgnoreCase(grouping)).findFirst().orElse(null);
	}

	private void remove(String grouping) {
		Filter filter = filter(grouping);
		if (filter != null) filters.remove(filter);
	}

	private long calculateItemCount(String condition) {
		if (source instanceof TemporalDatasource) return ((TemporalDatasource) source).itemCount(timetag, condition, filters);
		return source.itemCount(condition, filters);
	}

}