package io.intino.alexandria.sumus.filters;

import io.intino.alexandria.sumus.Filter;

import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class CompositeFilter implements Filter {
	private final List<Filter> filters;

	public CompositeFilter(Filter... filters) {
		this.filters = stream(filters).filter(f->f!=Filter.None).collect(toList());
	}

	@Override
	public boolean accepts(int idx) {
		return filters.stream().allMatch(f -> f.accepts(idx));
	}

	@Override
	public String toString() {
		return filters.stream().map(Object::toString).collect(joining(" | "));
	}
}
