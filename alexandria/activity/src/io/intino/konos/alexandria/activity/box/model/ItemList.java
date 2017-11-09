package io.intino.konos.alexandria.activity.box.model;

import io.intino.konos.alexandria.activity.box.displays.providers.ElementViewDisplayProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemList {
	private List<Item> items;
	private ElementViewDisplayProvider.Sorting sorting = null;

	public ItemList() {
		this.items = new ArrayList<>();
	}

	public ItemList(List<Item> items) {
		this.items = items;
	}

	public int count() {
		return items.size();
	}

	public void filter(Function<Item, Boolean> filter) {
		items = items.stream().filter(filter::apply).collect(Collectors.toList());
	}

	public ItemList sort(ElementViewDisplayProvider.Sorting sorting) {
		if (sorting == null || this.sorting == sorting) return this;

		this.sorting = sorting;
		items.sort(sorting::comparator);

		if (sorting.mode() == ElementViewDisplayProvider.Sorting.Mode.Descendant)
			Collections.reverse(items);

		return this;
	}

	public List<Item> items() {
		return items;
	}

	public List<Item> items(int start, int limit) {
		int end = start + limit;
		if (end > items.size()) end = items.size();
		return items.subList(start, end);
	}

	public List<Item> items(int start, int limit, ElementViewDisplayProvider.Sorting sorting) {
		sort(sorting);
		return items(start, limit);
	}
}
