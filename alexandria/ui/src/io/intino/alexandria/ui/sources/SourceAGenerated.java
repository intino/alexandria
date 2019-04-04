package io.intino.alexandria.ui.sources;

import io.intino.alexandria.ui.documentation.Item;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.Grouping;
import io.intino.alexandria.ui.model.datasource.Sorting;

import java.util.List;

public abstract class SourceAGenerated extends Datasource<Item> {

	public SourceAGenerated() {
		add(price());
		add(date());
		add(sorting1());
	}

	public abstract List<Item> items(int start, int limit, String condition);
	public abstract Grouping<Item> price();
	public abstract Grouping<Item> date();
	public abstract Sorting<Item> sorting1();

}
