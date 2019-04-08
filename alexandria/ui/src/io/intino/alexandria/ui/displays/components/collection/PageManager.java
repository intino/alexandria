package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.model.Datasource;

import java.util.List;

public class PageManager<Item> {
	private final Datasource source;
	private final int pageSize;
	private int page;
	private int countItems;
	private String condition;

	public PageManager(Datasource source, int pageSize) {
		this.source = source;
		this.pageSize = pageSize;
		this.page = 0;
	}

	public int page() {
		return this.page;
	}

	public synchronized List<Item> page(int index) {
		this.page = index;
		this.checkPageRange();
		return source.items(start(), limit(), condition);
//		if (isNearToEnd()) {
//			int count = countItems;
//			notifyNearToEnd();
//			if (count < PageSize && countItems() >= PageSize) refresh();
//			else sendCount(countItems());
//		}
	}

	public PageManager countItems(int countItems) {
		this.countItems = countItems;
		return this;
	}

	public PageManager condition(String condition) {
		this.condition = condition;
		return this;
	}

	public List<Item> previous() {
		return page(page--);
	}

	public List<Item> next() {
		return page(page++);
	}

	public List<Item> first() {
		return page(0);
	}

	public List<Item> last() {
		return page(countPages()-1);
	}

	public boolean isNearToEnd() {
		return page >= countPages()-2;
	}

	public int countPages() {
		if (pageSize <= 0) return 0;
		return (int) (Math.floor(countItems / pageSize) + (countItems % pageSize > 0 ? 1 : 0));
	}

	private void checkPageRange() {
		if (this.page <= 0)
			this.page = 0;

		int countPages = countPages();
		if (this.page >= countPages && countPages > 0)
			this.page = countPages - 1;
	}

	private int start() {
		return this.page * this.pageSize;
	}

	private int limit() {
		return this.pageSize;
	}

}
