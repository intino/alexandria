package io.intino.alexandria.ui.displays.components.collection.loaders;

import io.intino.alexandria.ui.model.datasource.DynamicTableDatasource;
import io.intino.alexandria.ui.model.datasource.temporal.TemporalDynamicTableDatasource;
import io.intino.alexandria.ui.model.dynamictable.Section;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.emptyList;

public class DynamicTableItemLoader<Item> extends ItemLoader<DynamicTableDatasource<Item>, Item> {
	private int pageSize;
	private String dimension;
	private String drill;
	private Section section;
	private String row;

	public DynamicTableItemLoader(DynamicTableDatasource<Item> source, int pageSize) {
		super(source);
		this.pageSize = pageSize;
	}

	public String dimension() {
		return this.dimension;
	}

	public DynamicTableItemLoader dimension(String dimension) {
		this.dimension = dimension;
		return this;
	}

	public String drill() {
		return this.drill;
	}

	public DynamicTableItemLoader drill(String drill) {
		this.drill = drill;
		return this;
	}

	public DynamicTableItemLoader section(Section section) {
		this.section = section;
		return this;
	}

	public DynamicTableItemLoader row(String row) {
		this.row = row;
		return this;
	}

	public int pageSize() {
		return pageSize;
	}

	public DynamicTableItemLoader pageSize(int size) {
		this.pageSize = size;
		return this;
	}

	public List<Section> sections() {
		if (source instanceof TemporalDynamicTableDatasource)
			return timetag != null ? ((TemporalDynamicTableDatasource) source).sections(timetag, dimension, drill, condition, filters) : emptyList();
		return ((DynamicTableDatasource) source).sections(dimension, drill, condition, filters);
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

	protected long calculateItemCount(String condition) {
		if (section == null || row == null) return 0;
		if (source instanceof TemporalDynamicTableDatasource)
			return timetag != null ? ((TemporalDynamicTableDatasource) source).itemCount(timetag, section, row, condition, filters) : 0;
		else if (source instanceof DynamicTableDatasource) return ((DynamicTableDatasource) source).itemCount(section, row, condition, filters);
		return 0;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Item> items(String... sortings) {
		int itemCount = Long.valueOf(itemCount()).intValue();
		List<String> sortingList = Arrays.asList(sortings);
		if (source instanceof TemporalDynamicTableDatasource)
			return timetag != null ? ((TemporalDynamicTableDatasource) source).items(timetag, 0, itemCount, section, row, condition, filters, sortingList) : emptyList();
		else if (source instanceof DynamicTableDatasource) return ((DynamicTableDatasource) source).items(0, itemCount, section, row, condition, filters, sortingList);
		return emptyList();
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
		if (source instanceof TemporalDynamicTableDatasource)
			return timetag != null ? ((TemporalDynamicTableDatasource<Item>) source).items(timetag, start, pageSize, section, row, condition, filters, sortings) : Collections.emptyList();
		return ((DynamicTableDatasource)source).items(start, pageSize, section, row, condition, filters, sortings);
	}

}