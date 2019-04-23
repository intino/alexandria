package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.model.Datasource;

import java.util.List;

public class ItemLoader<Item> {
	private final Datasource source;
	private int pageSize;
	private int countItems;
	private String condition;

	public ItemLoader(Datasource source, int pageSize) {
		this.source = source;
		this.pageSize = pageSize;
	}

	public ItemLoader<Item> pageSize(int size) {
		this.pageSize = size;
		return this;
	}

	public synchronized List<Item> page(int page) {
		page = this.checkPageRange(page);
		return source.items(start(page), count(), condition);
	}

	public ItemLoader countItems(int countItems) {
		this.countItems = countItems;
		return this;
	}

	public ItemLoader condition(String condition) {
		this.condition = condition;
		return this;
	}

	public int countPages() {
		if (pageSize <= 0) return 0;
		return pageOf(countItems);
	}

	private int checkPageRange(int page) {
		if (page <= 0)
			page = 0;

		int countPages = countPages();
		if (page >= countPages && countPages > 0)
			page = countPages - 1;
		return page;
	}

	private int start(int page) {
		return page * this.pageSize;
	}

	private int count() {
		return this.pageSize;
	}

	public List<Item> moreItems(int start, int stop) {
		return source.items(start, stop-start+1, condition);
	}

	private int pageOf(int index) {
		return (int) (Math.floor(index / pageSize) + (index % pageSize > 0 ? 1 : 0));
	}
}