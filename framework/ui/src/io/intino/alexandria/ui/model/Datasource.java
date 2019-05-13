package io.intino.alexandria.ui.model;

import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.Group;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;

public abstract class Datasource<O> {
	private List<O> items = new ArrayList<>();

	public void clear() {
		this.items.clear();
	}

	public long itemCount() {
		return itemCount(null, emptyList());
	}
	public abstract long itemCount(String condition, List<Filter> filters);
	public abstract List<O> items(int start, int count, String condition, List<Filter> filters);
	public abstract List<Group> groups(String key);

	public Datasource<O> addAll(List<O> items) {
		this.items.addAll(items);
		return this;
	}

	public Datasource<O> add(O item) {
		this.items.add(item);
		return this;
	}

}
