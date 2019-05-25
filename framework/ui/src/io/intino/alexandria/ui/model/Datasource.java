package io.intino.alexandria.ui.model;

import io.intino.alexandria.ui.model.datasource.Filter;
import io.intino.alexandria.ui.model.datasource.Group;

import java.util.List;

import static java.util.Collections.emptyList;

public abstract class Datasource<O> {

	public long itemCount() {
		return itemCount(null, emptyList());
	}
	public abstract long itemCount(String condition, List<Filter> filters);
	public abstract List<Group> groups(String key);

}
