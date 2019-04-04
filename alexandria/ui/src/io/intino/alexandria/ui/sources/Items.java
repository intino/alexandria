package io.intino.alexandria.ui.sources;

import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Category;
import io.intino.alexandria.ui.model.datasource.Group;

import java.util.List;

public abstract class Items extends AbstractItems {

	@Override
	public List<Item> items(int start, int limit, String condition) {
		return null;
	}



}