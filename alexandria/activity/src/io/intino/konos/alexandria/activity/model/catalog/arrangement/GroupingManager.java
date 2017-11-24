package io.intino.konos.alexandria.activity.model.catalog.arrangement;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.Catalog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class GroupingManager {
	private final List<Item> items;
	private final Map<String, Grouping> groupings;
	private final Map<Grouping, GroupMap> groups;
	private Map<String, List<String>> filteredGroupings = new HashMap<>();
	private Catalog.ArrangementFilterer filter;

	public GroupingManager(List<Item> items, List<Grouping> groupings, Catalog.ArrangementFilterer filter) {
		this.items = items;
		this.filter = filter;
		this.groupings = groupings.stream().collect(toMap(Grouping::name, g -> g));
		this.groups = calculateGroupings(items);
	}

	private Map<Grouping, GroupMap> calculateGroupings(List<Item> items) {
		String username = filter != null ? filter.username() : null;
		List<Item> groupingManagerItems = this.items;
		return this.groupings.values().stream().collect(toMap(g -> g, g -> filteredGroupings.keySet().contains(g.name()) ? g.groups(groupingManagerItems, username) : g.groups(items, username)));
	}

	public void filter(String groupingName, List<String> groups) {
		Grouping grouping = groupings.get(groupingName);
		List<String> groupNames = groups.stream().map(Group::name).collect(toList());
		filter.add(collect(groupNames, grouping));
		filteredGroupings.put(groupingName, groupNames);
	}

	public void clearFilter() {
		filter.clear();
		filteredGroupings.clear();
	}

	public List<Item> items() {
		if (filter.isEmpty()) return items;
		return items.stream().filter(item -> filter.contains(item.id())).collect(toList());
	}

	public Map<Grouping, GroupMap> groups() {
		if (filter.isEmpty()) return groups;
		return calculateGroupings(items());
	}

	public List<String> filteredGroups(Grouping grouping) {
		String key = grouping.name();
		return filteredGroupings.containsKey(key) ? filteredGroupings.get(key) : emptyList();
	}

	public GroupMap groups(Grouping grouping) {
		return groups(grouping.name());
	}

	public GroupMap groups(String grouping) {
		return groups().entrySet().stream()
				.filter(e -> e.getKey().name().equals(grouping) || e.getKey().label().equals(grouping))
				.map(Map.Entry::getValue).findFirst().orElse(null);
	}

	private Group[] collect(List<String> groups, Grouping grouping) {
		return groups.stream().map(c -> this.groups.get(grouping).get(c)).toArray(Group[]::new);
	}

}
