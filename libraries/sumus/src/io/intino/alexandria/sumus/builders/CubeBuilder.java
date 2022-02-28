package io.intino.alexandria.sumus.builders;

import io.intino.alexandria.sumus.builders.accumulators.BuilderAccumulator;
import io.intino.alexandria.sumus.builders.accumulators.CountAccumulator;
import io.intino.alexandria.sumus.builders.accumulators.IntegerAccumulator;
import io.intino.alexandria.sumus.builders.accumulators.NumberAccumulator;
import io.intino.alexandria.sumus.filters.CompositeFilter;
import io.intino.alexandria.sumus.filters.SliceFilter;
import io.intino.alexandria.sumus.*;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

public class CubeBuilder {
	private final Ledger ledger;
	private final Filter filter;
	private final List<Dimension> dimensions;

	public CubeBuilder(Ledger ledger, Filter filter, Dimension... dimensions) {
		this(ledger, filter, asList(dimensions));
	}

	public CubeBuilder(Ledger ledger, Filter filter, List<Dimension> dimensions) {
		this.ledger = ledger;
		this.filter = filter;
		this.dimensions = dimensions;
	}

	public List<CellBuilder> builders() {
		List<CellBuilder> result = new ArrayList<>();
		int size = dimensions.size();
		for (int i = 0; i < size; i++)
			result.addAll(builders(List.of(), dimensions.subList(i, size)));
		result.add(new CellBuilder(filter));
		return result;
	}

	private List<CellBuilder> builders(List<Slice> slices, List<Dimension> dimensions) {
		List<CellBuilder> result = export(slices);
		if (dimensions.isEmpty()) return result;
		List<Slice> head = filter.crop(dimensions.get(0).slices());
		List<Dimension> tail = dimensions.subList(1, dimensions.size());
		for (Slice slice : head) {
			List<CellBuilder> cells = builders(join(slices, slice), tail);
			result.addAll(cells);
		}
		return result;
	}

	private List<CellBuilder> export(List<Slice> slices) {
		List<CellBuilder> result = slices.isEmpty() ? List.of() : List.of(new CellBuilder(filter, slices));
		return new ArrayList<>(result);
	}

	private List<Slice> join(List<Slice> slices,Slice slice) {
		List<Slice> result = new ArrayList<>(slices);
		result.add(slice);
		return result;
	}

	public Cube get() {
		List<CellBuilder> builders = builders();
		builders.forEach(this::createAccumulatorsOf);
		ledger.facts(filter).forEach(fact -> add(builders, fact));
		return cube(builders);
	}

	private void add(List<CellBuilder> builders, Fact fact) {
		builders.forEach(b->b.add(fact));
	}

	private void createAccumulatorsOf(CellBuilder builder) {
		ledger.attributes().forEach(a->builder.add(accumulatorOf(a)));
	}

	private BuilderAccumulator accumulatorOf(Attribute attribute) {
		if (attribute.type == Attribute.Type.integer) return new IntegerAccumulator(attribute.name);
		if (attribute.type == Attribute.Type.number) return new NumberAccumulator(attribute.name);
		return new CountAccumulator(attribute.name);
	}

	private Cube cube(List<CellBuilder> builders) {
		return new Cube() {
			private final List<Cell> cells = buildCells();

			@Override
			public List<Dimension> dimensions() {
				return dimensions;
			}

			public List<Cell> cells() {
				return cells;
			}

			@Override
			public Iterable<Fact> facts(Filter filter) {
				return ledger.facts(new CompositeFilter(CubeBuilder.this.filter, filter));
			}

			private List<Cell> buildCells() {
				return builders.stream()
						.map(b -> b.cell(facts(new CompositeFilter(filter, filterOf(b.slices)))))
						.collect(toList());
			}

			private Filter filterOf(List<Slice> slices) {
				return slices.isEmpty() ? Filter.None : new SliceFilter(slices);
			}

		};
	}


}
