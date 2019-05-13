package io.intino.alexandria.ui.model;

import io.intino.alexandria.ui.displays.providers.ElementViewDisplayProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemList {
	private List<Item> items;
	private ElementViewDisplayProvider.Sorting sorting = null;
	private boolean sorted = false;

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

	public void filterCount(int maxItems) {
		items = items.subList(0, maxItems > items.size() ? items.size() : maxItems);
	}

	public ItemList sort(ElementViewDisplayProvider.Sorting sorting) {
		if (sorting == null) return this;
		if (sorted && this.sorting == sorting) return this;

		this.sorting = sorting;
		items.sort(sorting::comparator);

		if (sorting.mode() == ElementViewDisplayProvider.Sorting.Mode.Descendant)
			Collections.reverse(items);

		sorted = true;
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

	public int size() {
		return items.size();
	}

	public void addAll(ItemList itemList) {
		items.addAll(itemList.items);
		sorted = false;
	}
}
