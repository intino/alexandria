package io.intino.alexandria.sumus.filters;

import io.intino.alexandria.sumus.Dimension;
import io.intino.alexandria.sumus.Filter;
import io.intino.alexandria.sumus.Index;
import io.intino.alexandria.sumus.Slice;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.*;

public class SliceFilter implements Filter {
	private final Set<Slice> slices;
	private final Set<Dimension> dimensions;
	private final Index index;

	public SliceFilter(Slice... slices) {
		this(asList(slices));
	}

	public SliceFilter(List<Slice> slices) {
		this.slices = new HashSet<>(slices);
		this.dimensions = slices.stream().map(Slice::dimension).collect(toSet());
		this.index = new Slice.SliceIndex(this.slices);
	}


	@Override
	public boolean accepts(int idx) {
		return index.contains(idx);
	}

	@Override
	public List<Slice> crop(List<Slice> slices) {
		return slices.stream().filter(this::accepts).collect(toList());
	}

	private boolean accepts(Slice slice) {
		return !dimensions.contains(slice.dimension()) || contains(slice);
	}

	private boolean contains(Slice slice) {
		return slice != null && (slices.contains(slice) || contains(slice.parent()));
	}

	@Override
	public String toString() {
		return slices.stream().map(Slice::name).collect(joining(" | "));
	}
}
