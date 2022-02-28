package io.intino.alexandria.sumus.ledgers.composite;

import io.intino.alexandria.sumus.Index;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CompositeIndex implements Index {
	private final List<Index> indexes;
	private final int[] offsets;

	public CompositeIndex(List<Index> indexes, int[] offsets) {
		this.indexes = indexes.stream().map(this::wrap).collect(toList());
		this.offsets = offsets;
	}

	@Override
	public boolean contains(int idx) {
		int pos = posOf(idx);
		return indexes.get(pos).contains(idx - offsets[pos]);
	}

	private int posOf(int idx) {
		int pos = offsets.length - 1;
		while (idx < offsets[pos]) pos--;
		return pos;
	}

	private Index wrap(Index i) {
		return i != null ? i : Index.None;
	}

}
