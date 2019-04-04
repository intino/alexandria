package io.intino.alexandria.ui.sources;

import io.intino.alexandria.ui.documentation.Item;
import io.intino.alexandria.ui.model.datasource.Group;
import io.intino.alexandria.ui.model.datasource.Grouping;
import io.intino.alexandria.ui.model.datasource.Sorting;

import java.util.List;
import java.util.function.Predicate;

public class SourceA extends SourceAGenerated {

	@Override
	public List<Item> items(int start, int limit, String condition) {
		return null;
	}

	@Override
	public Grouping<Item> price() {
		return new Grouping<Item>() {
			@Override
			public List<Group> groups(List<Item> items) {
				return null;
			}

			@Override
			public Predicate filter(List<Item> items) {
				return null;
			}
		};
	}

	@Override
	public Grouping<Item> date() {
		return null;
	}

	@Override
	public Sorting<Item> sorting1() {
		return null;
	}

}
