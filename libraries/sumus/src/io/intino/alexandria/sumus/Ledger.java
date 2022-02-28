package io.intino.alexandria.sumus;

import io.intino.alexandria.sumus.filters.SliceFilter;
import io.intino.alexandria.sumus.helpers.Finder;

import java.util.*;

import static java.util.Arrays.asList;

public interface Ledger {
	int size();
	List<Attribute> attributes();
	default Attribute attribute(String name) { return new Finder<>(attributes()).find(name); }
	List<Dimension> dimensions();
	default Dimension dimension(String name) { return new Finder<>(dimensions()).find(name); }
	default Iterable<Fact> facts() {return facts(Filter.None); }
	Iterable<Fact> facts(Filter filter);

	Query cube();

	interface Query {
		Query filter(Filter filter);
		default Query filter(Slice... slices) { return filter(new SliceFilter(slices)); }
		Query dimensions(List<Dimension> dimensions);
		default Query dimensions(Dimension... dimensions) { return dimensions(asList(dimensions)); }

		Cube build();
	}

}
