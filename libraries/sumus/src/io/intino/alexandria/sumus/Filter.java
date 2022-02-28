package io.intino.alexandria.sumus;

import java.util.List;

public interface Filter {
	Filter None = idx -> true;

	boolean accepts(int idx);
	default int size() {
		return 0;
	}
	default List<Slice> crop(List<Slice> slices) {
		return slices;
	}


}
