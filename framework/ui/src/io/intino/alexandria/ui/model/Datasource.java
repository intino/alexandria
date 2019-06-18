package io.intino.alexandria.ui.model;

import io.intino.alexandria.ui.model.datasource.Group;

import java.util.List;

public abstract class Datasource<O> {

	public abstract List<Group> groups(String key);

}
