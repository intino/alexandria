package io.intino.alexandria.sumus.ledgers.composite;

import io.intino.alexandria.sumus.Attribute;
import io.intino.alexandria.sumus.Dimension;
import io.intino.alexandria.sumus.Slice;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CompositeDimension implements Dimension {
	private final List<Dimension> dimensions;
	private final List<Slice> slices;
	private final int[] offsets;

	public CompositeDimension(List<Dimension> dimensions, int[] offsets) {
		this.dimensions = dimensions;
		this.slices = new ArrayList<>();
		this.offsets = offsets;
	}
	@Override
	public String name() {
		return dimensions.get(0).name();
	}

	@Override
	public Attribute.Type type() {
		return dimensions.get(0).type();
	}

	@Override
	public boolean hasNA() {
		return dimensions.stream().anyMatch(Dimension::hasNA);
	}

	@Override
	public List<Slice> slices() {
		if (slices.isEmpty()) slices.addAll(buildSlices());
		return slices;
	}

	private List<Slice> buildSlices() {
		return sliceNames().stream().map(s->new CompositeSlice(find(s),offsets)).collect(toList());
	}

	private List<Slice> find(String name) {
		return dimensions.stream().map(d->find(name,d)).collect(toList());
	}

	private Slice find(String name, Dimension dimension) {
		return dimension.slices().stream()
				.filter(s->s.name().equals(name))
				.findFirst()
				.orElse(null);
	}

	private List<String> sliceNames() {
		return dimensions.stream()
				.flatMap(d->d.slices().stream())
				.map(Slice::name)
				.distinct()
				.collect(toList());
	}

	@Override
	public String toString() {
		return name() + ":" + type() + "[" + dimensions.size() + "]";
	}
}
