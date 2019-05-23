package io.intino.alexandria.ui.displays.components.collection;

import io.intino.alexandria.ui.model.Datasource;

import java.util.ArrayList;
import java.util.List;

public class PageItemLoader<DS extends Datasource<Item>, Item> extends ItemLoader<DS, Item> {
	private int pageSize;

	public PageItemLoader(DS source, int pageSize) {
		super(source);
		this.pageSize = pageSize;
	}

	public PageItemLoader pageSize(int size) {
		this.pageSize = size;
		return this;
	}

	public synchronized List<Item> page(int page) {
		page = this.checkPageRange(page);
		return source.items(start(page), pageSize, condition, filters, new ArrayList<>(sortings));
	}

	public int pageCount() {
		if (pageSize <= 0) return 0;
		return pageOf(itemCount());
	}

	public int start(int page) {
		return page * this.pageSize;
	}

	public int pageOf(long index) {
		return (int) (Math.floor(index / pageSize) + (index % pageSize > 0 ? 1 : 0));
	}

	private int checkPageRange(int page) {
		if (page <= 0)
			page = 0;

		int countPages = pageCount();
		if (page >= countPages && countPages > 0)
			page = countPages - 1;
		return page;
	}

}