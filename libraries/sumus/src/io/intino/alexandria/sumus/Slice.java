package io.intino.alexandria.sumus;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.groupingBy;

public interface Slice {
	default	Slice parent() { return null; }
	String name();
	Dimension dimension();
	default int level() { return 1; }

	boolean isNA();
	Index index();

	class SliceIndex implements Index {
		private final Collection<List<Slice>> slices;

		public SliceIndex(Set<Slice> slices) {
			this.slices = groupedByDimension(slices);
		}

		@Override
		public boolean contains(int idx) {
			return slices.stream().allMatch(s->contains(s,idx));
		}

		private boolean contains(List<Slice> slices, int idx) {
			return slices.stream().anyMatch(s->s.index().contains(idx));
		}

		private Collection<List<Slice>> groupedByDimension(Set<Slice> slices) {
			return slices.stream()
					.collect(groupingBy(Slice::dimension))
					.values();
		}

	}
}
