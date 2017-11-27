package io.intino.konos.alexandria.activity.model.catalog.arrangement;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class GroupingManager {
	private List<Item> items;
	private final List<Grouping> groupings;
	private Map<Grouping, GroupMap> groups;
	private Map<String, List<String>> filteredGroupings = new HashMap<>();
	private Catalog.ArrangementFilterer filter;
	private String fixedGrouping = null;

	public GroupingManager(List<Item> items, List<Grouping> groupings, Catalog.ArrangementFilterer filter) {
		this.items = items;
		this.filter = filter;
		this.groupings = groupings;
		this.groups = calculateGroupings(items);
	}

	private Map<Grouping, GroupMap> calculateGroupings(List<Item> items) {
		String username = filter != null ? filter.username() : null;
		List<Item> groupingManagerItems = this.items;
		return this.groupings.stream().collect(toMap(g -> g, g -> filteredGroupings.keySet().contains(g.name()) ? g.groups(groupingManagerItems, username) : g.groups(items, username)));
	}

	public void filter(String groupingName, List<String> groups) {
		Grouping grouping = grouping(groupingName);
		List<String> groupNames = groups.stream().map(Group::name).collect(toList());
		filter.add(collect(groupNames, grouping));
		filteredGroupings.put(groupingName, groupNames);
		fixedGrouping = groupingName;
	}

	public void clearFilter() {
		filter.clear();
		filteredGroupings.clear();
		fixedGrouping = null;
	}

	public void items(List<Item> items) {
		this.items = items;
	}

	public List<String> filteredGroups(Grouping grouping) {
		String key = grouping.name();
		return filteredGroupings.containsKey(key) ? filteredGroupings.get(key) : emptyList();
	}

	public GroupMap groups(Grouping grouping) {
		return groups(grouping.name());
	}

	public GroupMap groups(String grouping) {
		List<Item> items = this.items;

		if ((fixedGrouping == null && !groupings.get(0).name().equals(grouping))
			|| (fixedGrouping != null && !fixedGrouping.equals(grouping)))
			items = filteredItems();

		return groups(items).entrySet().stream()
				.filter(e -> e.getKey().name().equals(grouping) || e.getKey().label().equals(grouping))
				.map(Map.Entry::getValue).findFirst().orElse(null);
	}

	private Group[] collect(List<String> groups, Grouping grouping) {
		return groups.stream().map(c -> this.groups.get(grouping).get(c)).filter(Objects::nonNull).toArray(Group[]::new);
	}

	private Map<Grouping, GroupMap> groups(List<Item> items) {
		if (filter.isEmpty()) return groups;
		return calculateGroupings(items);
	}

	private Grouping grouping(String groupingName) {
		return groupings.stream().filter(g -> g.name().equals(groupingName)).findFirst().orElse(null);
	}

	private List<Item> filteredItems() {
		if (filter.isEmpty()) return items;
		return items.stream().filter(item -> filter.contains(item.id())).collect(toList());
	}

}
