package io.intino.alexandria.event.tuple;

import io.intino.alexandria.event.Event;
import io.intino.alexandria.ztp.Tuple;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class TupleEvent implements Event {

	public static final int MIN_SIZE = 5;

	private final Tuple tuple;

	public TupleEvent(Tuple tuple) {
		if(tuple.size() < MIN_SIZE) throw new IllegalArgumentException("A tuple event must have at least " + MIN_SIZE + " fields");
		this.tuple = tuple;
	}

	public String subject() {
		return tuple.get(0);
	}

	public String predicate() {
		return tuple.get(1);
	}

	public String value() {
		return tuple.get(2);
	}

	@Override
	public Instant ts() {
		return Instant.parse(tuple.get(3));
	}

	@Override
	public String ss() {
		return tuple.get(4);
	}

	@Override
	public Format format() {
		return Format.Tuple;
	}

	public String get(int index) {
		return tuple.get(index);
	}

	public List<String> fields() {
		return tuple.fields();
	}

	public Tuple toTuple() {
		return tuple;
	}

	@Override
	public String toString() {
		return tuple.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TupleEvent that = (TupleEvent) o;
		return Objects.equals(tuple, that.tuple);
	}

	@Override
	public int hashCode() {
		return Objects.hash(tuple);
	}
}
