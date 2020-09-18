package io.intino.alexandria.ui.displays.components.collection.loaders;

import io.intino.alexandria.Timetag;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.filters.RangeFilter;
import io.intino.alexandria.ui.model.datasource.filters.GroupFilter;
import io.intino.alexandria.ui.model.datasource.MapDatasource;
import io.intino.alexandria.ui.model.datasource.MemoryDatasource;
import io.intino.alexandria.ui.model.datasource.PageDatasource;
import io.intino.alexandria.ui.model.datasource.temporal.TemporalDatasource;
import io.intino.alexandria.ui.model.datasource.temporal.TemporalPageDatasource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static java.util.Collections.emptyList;

public class ItemLoader<DS extends Datasource<Item>, Item> {
	private long itemCount;
	final DS source;
	String condition;
	List<Filter> filters = new ArrayList<>();
	HashSet<String> sortings = new HashSet<>();
	Timetag timetag = null;

	public ItemLoader(DS source) {
		this.source = source;
		this.itemCount = calculateItemCount(null);
	}

	public ItemLoader reload() {
		this.itemCount = calculateItemCount(condition);
		return this;
	}

	public ItemLoader clearFilters() {
		condition = null;
		filters.clear();
		this.itemCount = calculateItemCount(condition);
		return this;
	}

	public java.util.List<Filter> filters() {
		return filters;
	}

	public ItemLoader filter(String grouping, List<String> groups) {
		if (groups.size() <= 0) remove(grouping);
		else {
			Filter filter = filter(grouping);
			if (filter == null) filters.add(new GroupFilter(grouping, groups));
			else ((GroupFilter)filter).groups(groups);
		}
		this.itemCount = calculateItemCount(condition);
		return this;
	}

	public ItemLoader filter(String grouping, Instant from, Instant to) {
		Filter filter = filter(grouping);
		if (filter == null) filters.add(new RangeFilter(grouping, from, to));
		else ((RangeFilter)filter).from(from).to(to);
		this.itemCount = calculateItemCount(condition);
		return this;
	}

	public void removeFilter(String grouping) {
		remove(grouping);
		this.itemCount = calculateItemCount(condition);
	}

	public String condition() {
		return condition;
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

	@SuppressWarnings("unchecked")
	public List<Item> items(String... sortings) {
		int itemCount = Long.valueOf(itemCount()).intValue();
		List<String> sortingList = Arrays.asList(sortings);
		if (source instanceof TemporalDatasource)
			return timetag != null ? ((TemporalDatasource) source).items(timetag, 0, itemCount, condition, filters, sortingList) : emptyList();
		else if (source instanceof TemporalPageDatasource)
			return timetag != null ? ((TemporalPageDatasource) source).items(timetag, 0, itemCount, condition, filters, sortingList) : emptyList();
		else if (source instanceof MemoryDatasource) return ((MemoryDatasource) source).items(0, itemCount, condition, filters, sortingList);
		else if (source instanceof PageDatasource) return ((PageDatasource) source).items(0, itemCount, condition, filters, sortingList);
		return emptyList();
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
		if (source instanceof TemporalDatasource)
			return timetag != null ? ((TemporalDatasource) source).itemCount(timetag, condition, filters) : 0;
		else if (source instanceof TemporalPageDatasource)
			return timetag != null ? ((TemporalPageDatasource) source).itemCount(timetag, condition, filters) : 0;
		else if (source instanceof MemoryDatasource) return ((MemoryDatasource) source).itemCount(condition, filters);
		else if (source instanceof MapDatasource) return ((MapDatasource) source).placeMarkCount(condition, filters);
		else if (source instanceof PageDatasource) return ((PageDatasource) source).itemCount(condition, filters);
		return 0;
	}

}