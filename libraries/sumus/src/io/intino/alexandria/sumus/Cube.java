package io.intino.alexandria.sumus;

import io.intino.alexandria.sumus.helpers.Finder;

import java.util.List;

public interface Cube {
	List<Dimension> dimensions();
	List<Cell> cells();
	default Cell cell(String name) { return new Finder<>(cells()).find(name); }
	Iterable<Fact> facts(Filter filter);
	default Iterable<Fact> facts() { return facts(Filter.None); }

	interface Cell {
		List<Slice> slices();
		List<Indicator> indicators();
		default Indicator indicator(String name) {
			return new Finder<>(indicators()).find(name);
		}

		Iterable<Fact> facts();
	}

	interface Indicator {
		String name();
		Object value();
	}

}
