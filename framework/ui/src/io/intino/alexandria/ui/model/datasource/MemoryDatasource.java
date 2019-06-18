package io.intino.alexandria.ui.model.datasource;

import io.intino.alexandria.ui.model.Datasource;

import java.util.ArrayList;
import java.util.List;

public abstract class MemoryDatasource<O> extends Datasource<O> {
	private List<O> items = new ArrayList<>();

	public void add(O item) {
		this.items.add(item);
	}

	public void addAll(List<O> items) {
		this.items.addAll(items);
	}

	public abstract long itemCount(String condition, List<Filter> filters);
	public abstract List<O> items(int start, int count, String condition, List<Filter> filters, List<String> sortings);

}
