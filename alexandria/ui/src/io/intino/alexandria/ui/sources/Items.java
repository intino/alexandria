package io.intino.alexandria.ui.sources;

import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Group;
import io.intino.alexandria.ui.model.datasource.Grouping;
import io.intino.alexandria.ui.model.datasource.Sorting;

import java.util.List;

public class Items extends AbstractItems {

	@Override
	public int countItems(String condition) {
		return 0;
	}

	@Override
	public List<io.intino.alexandria.ui.documentation.Item> items(int start, int limit, String condition) {
		return null;
	}



}