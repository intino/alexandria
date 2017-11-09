package io.intino.konos.alexandria.activity.model.catalog.arrangement;

import io.intino.konos.alexandria.activity.model.Item;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Grouping extends Arrangement {
	private boolean cluster;
	private Histogram histogram = Histogram.Percentage;
	private GroupLoader groupLoader;
	private GroupManager groupManager;

	public boolean cluster() {
		return cluster;
	}

	public Grouping cluster(boolean cluster) {
		this.cluster = cluster;
		return this;
	}

	public Histogram histogram() {
		return histogram;
	}

	public Grouping histogram(Histogram histogram) {
		this.histogram = histogram;
		return this;
	}

	public Grouping groupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
		return this;
	}

	public Group remove(String group) {
		return groupManager != null ? groupManager.remove(group) : null;
	}

	public GroupMap groups(List<Item> items) {
		List<Object> objects = items.stream().map(Item::object).collect(toList());
		return groupLoader != null ? new GroupMap().putAll(groupLoader.load(objects)) : new GroupMap();
	}

	public Grouping groups(GroupLoader loader) {
		this.groupLoader = loader;
		return this;
	}

	public interface GroupManager {
		void add(String group);
		Group remove(String group);
	}

	public interface GroupLoader {
		List<Group> load(List<Object> items);
	}

	public enum Histogram {
		Percentage, Absolute;
	}

}
