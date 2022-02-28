package io.intino.alexandria.sumus.ledgers.composite;

import io.intino.alexandria.sumus.Dimension;
import io.intino.alexandria.sumus.Index;
import io.intino.alexandria.sumus.Slice;

import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class CompositeSlice implements Slice {
	private final Slice prototype;
	private final List<Slice> slices;
	private final int[] offsets;
	private Index index;

	public CompositeSlice(List<Slice> slices, int[] offsets) {
		this.prototype = slices.stream().filter(Objects::nonNull).findFirst().orElse(null);
		this.slices = slices;
		this.offsets = offsets;
		this.index = null;
	}

	@Override
	public String name() {
		return prototype.name();
	}

	@Override
	public Dimension dimension() {
		return prototype.dimension();
	}

	@Override
	public boolean isNA() {
		return slices.stream().filter(Objects::nonNull).anyMatch(Slice::isNA);
	}

	@Override
	public Index index() {
		if (index == null) index = new CompositeIndex(indexes(), offsets);
		return index;
	}

	private List<Index> indexes() {
		return slices.stream().map(s->s==null?Index.None:s.index()).collect(toList());
	}

	@Override
	public String toString() {
		return name();
	}
}
