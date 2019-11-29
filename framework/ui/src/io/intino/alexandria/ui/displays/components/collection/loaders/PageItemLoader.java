package io.intino.alexandria.ui.displays.components.collection.loaders;

import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.PageDatasource;
import io.intino.alexandria.ui.model.datasource.temporal.TemporalPageDatasource;

import java.util.ArrayList;
import java.util.Collections;
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
		return items(start(page), pageSize);
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

	public List<Item> moreItems(int start, int stop) {
		return items(start, stop - start + 1);
	}

	private int checkPageRange(int page) {
		if (page <= 0) page = 0;
		int countPages = pageCount();
		if (page >= countPages && countPages > 0)
			page = countPages - 1;
		return page;
	}

	private List<Item> items(int start, int pageSize) {
		ArrayList<String> sortings = new ArrayList<>(this.sortings);
		if (source instanceof TemporalPageDatasource)
			return timetag != null ? ((TemporalPageDatasource<Item>) source).items(timetag, start, pageSize, condition, filters, sortings) : Collections.emptyList();
		return ((PageDatasource)source).items(start, pageSize, condition, filters, sortings);
	}

}