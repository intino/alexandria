package io.intino.konos.alexandria.framework.box.model.catalog.arrangement;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GroupMap extends HashMap<String, Group> {

	public GroupMap putAll(List<Group> groupList) {
		groupList.forEach(g -> put(g.name(), g));
		return this;
	}

	public List<Group> toList() {
		return this.values().stream().sorted(Comparator.comparing(Group::label)).collect(Collectors.toList());
	}
}
