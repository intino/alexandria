package io.intino.alexandria.ui.model;

import io.intino.alexandria.ui.model.datasource.Grouping;
import io.intino.alexandria.ui.model.datasource.Sorting;

import java.util.ArrayList;
import java.util.List;

public abstract class Datasource<O> {
	private List<O> items = new ArrayList<>();
	private List<Grouping> groupings = new ArrayList<>();
	private List<Sorting> sortings = new ArrayList<>();

	public void clear() {
		this.items.clear();
	}

	public int itemCount() {
		return itemCount(null);
	}

	public abstract int itemCount(String condition);
	public abstract List<O> items(int start, int count, String condition);

	public Datasource<O> addAll(List<O> items) {
		this.items.addAll(items);
		return this;
	}

	public Datasource<O> add(O item) {
		this.items.add(item);
		return this;
	}

	public Datasource<O> add(Grouping<O> grouping) {
		this.groupings.add(grouping);
		return this;
	}

	public Datasource<O> add(Sorting<O> sorting) {
		this.sortings.add(sorting);
		return this;
	}
}
