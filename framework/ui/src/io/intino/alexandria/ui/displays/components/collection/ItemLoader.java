package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Filter;

import java.util.*;

public class ItemLoader<Item> {
	private final Datasource<Item> source;
	private int pageSize;
	private long itemCount;
	private String condition;
	private List<Filter> filters = new ArrayList<>();
	private Set<String> sortings = new HashSet<>();

	public ItemLoader(Datasource source, int pageSize) {
		this.source = source;
		this.pageSize = pageSize;
		this.itemCount = source.itemCount();
	}

	public ItemLoader<Item> pageSize(int size) {
		this.pageSize = size;
		return this;
	}

	public synchronized List<Item> page(int page) {
		page = this.checkPageRange(page);
		return source.items(start(page), count(), condition, filters, new ArrayList<>(sortings));
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

	public int pageCount() {
		if (pageSize <= 0) return 0;
		return pageOf(itemCount);
	}

	public int start(int page) {
		return page * this.pageSize;
	}

	public int pageOf(long index) {
		return (int) (Math.floor(index / pageSize) + (index % pageSize > 0 ? 1 : 0));
	}

	public List<Item> moreItems(int start, int stop) {
		return source.items(start, stop - start + 1, condition, filters, new ArrayList<>(sortings));
	}

	private int checkPageRange(int page) {
		if (page <= 0)
			page = 0;

		int countPages = pageCount();
		if (page >= countPages && countPages > 0)
			page = countPages - 1;
		return page;
	}

	private int count() {
		return this.pageSize;
	}

	private Filter filter(String grouping) {
		return filters.stream().filter(f -> f.grouping().equalsIgnoreCase(grouping)).findFirst().orElse(null);
	}

	private void remove(String grouping) {
		Filter filter = filter(grouping);
		if (filter != null) filters.remove(filter);
	}
}