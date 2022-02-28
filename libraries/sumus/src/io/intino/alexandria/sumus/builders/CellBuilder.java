package io.intino.alexandria.sumus.builders;

import io.intino.alexandria.sumus.filters.CompositeFilter;
import io.intino.alexandria.sumus.filters.SliceFilter;
import io.intino.alexandria.sumus.Cube;
import io.intino.alexandria.sumus.Fact;
import io.intino.alexandria.sumus.Filter;
import io.intino.alexandria.sumus.Slice;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class CellBuilder {
	public final Filter filter;
	public final List<Slice> slices;
	private final List<Accumulator> accumulators;

	public CellBuilder(Filter filter) {
		this.filter = filter;
		this.slices = emptyList();
		this.accumulators = new ArrayList<>();
	}

	public CellBuilder(Filter filter, List<Slice> slices) {
		this.filter = new CompositeFilter(filter, new SliceFilter(slices));
		this.slices = slices;
		this.accumulators = new ArrayList<>();
	}

	public void add(Accumulator accumulator) {
		this.accumulators.add(accumulator);
	}

	public void add(Fact fact) {
		if (!filter.accepts(fact.id())) return;
		accumulators.forEach(a -> a.add(fact.value(a.name())));
	}

	@Override
	public String toString() {
		return slices.stream().map(Slice::name).collect(joining("-"));
	}

	public Cube.Cell cell(Iterable<Fact> facts) {
		return new Cube.Cell() {
			@Override
			public List<Slice> slices() {
				return slices;
			}

			@Override
			public List<Cube.Indicator> indicators() {
				return accumulators.stream().flatMap(a->a.indicators().stream()).collect(toList());
			}

			@Override
			public Iterable<Fact> facts() {
				return facts;
			}

			@Override
			public String toString() {
				return slices.stream()
						.map(Slice::toString)
						.collect(joining("-"));
			}
		};
	}

	public static class Indicator implements Cube.Indicator {
		private final String name;
		private final Object value;

		public Indicator(String name, Object value) {
			this.name = name;
			this.value = value;
		}

		@Override
		public String toString() {
			return name;
		}

		@Override
		public String name() {
			return name;
		}

		@Override
		public Object value() {
			return value;
		}
	}
}
