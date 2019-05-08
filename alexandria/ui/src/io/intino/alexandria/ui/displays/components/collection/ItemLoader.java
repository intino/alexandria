package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Filter;

import java.util.ArrayList;
import java.util.List;

public class ItemLoader<Item> {
	private final Datasource source;
	private int pageSize;
	private int itemCount;
	private String condition;
	private List<Filter> filters = new ArrayList<>();

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
		return source.items(start(page), count(), condition, filters);
	}

	public ItemLoader filter(String grouping, List<String> groups) {
		filters.add(new Filter(grouping, groups));
		return this;
	}

	public ItemLoader condition(String condition) {
		this.condition = condition;
		this.itemCount = source.itemCount(condition, filters);
		return this;
	}

	public int pageCount() {
		if (pageSize <= 0) return 0;
		return pageOf(itemCount);
	}

	private int checkPageRange(int page) {
		if (page <= 0)
			page = 0;

		int countPages = pageCount();
		if (page >= countPages && countPages > 0)
			page = countPages - 1;
		return page;
	}

	public int start(int page) {
		return page * this.pageSize;
	}

	public int pageOf(int index) {
		return (int) (Math.floor(index / pageSize) + (index % pageSize > 0 ? 1 : 0));
	}

	private int count() {
		return this.pageSize;
	}

	public List<Item> moreItems(int start, int stop) {
		return source.items(start, stop-start+1, condition, filters);
	}
}