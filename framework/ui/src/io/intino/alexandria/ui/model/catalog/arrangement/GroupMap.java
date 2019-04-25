package io.intino.alexandria.ui.model.catalog.arrangement;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GroupMap extends LinkedHashMap<String, Group> {

	public GroupMap putAll(List<Group> groupList) {
		groupList.forEach(g -> put(g.name(), g));
		return this;
	}

	public List<Group> toList() {
		return new ArrayList<>(this.values());
	}
}
