package io.intino.alexandria.ui.model.datasource.filters;

import io.intino.alexandria.ui.model.datasource.Filter;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;

public class RangeFilter extends Filter {
	private Instant from;
	private Instant to;

	public RangeFilter(String grouping, Instant from, Instant to) {
		super(grouping);
		this.from = from;
		this.to = to;
	}

	public Instant from() {
		return from;
	}

	public RangeFilter from(Instant from) {
		this.from = from;
		return this;
	}

	public Instant to() {
		return to;
	}

	public RangeFilter to(Instant to) {
		this.to = to;
		return this;
	}
}
