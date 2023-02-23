package io.intino.alexandria.event.tuple;

import io.intino.alexandria.event.Event;

import java.time.Instant;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

public class TupleEvent implements Event {

	public static final int MIN_SIZE = 5;

	private final String[] fields;

	public TupleEvent(String[] fields) {
		if(fields.length < MIN_SIZE) throw new IllegalArgumentException("A tuple must have at least " + MIN_SIZE + " fields");
		this.fields = fields;
	}

	public String subject() {
		return fields[0];
	}

	public String predicate() {
		return fields[1];
	}

	public String value() {
		return fields[2];
	}

	@Override
	public Instant ts() {
		return Instant.parse(fields[3]); // TODO
	}

	@Override
	public String ss() {
		return fields[4]; // TODO
	}

	@Override
	public Format format() {
		return Format.Tuple;
	}

	public String get(int index) {
		return fields[index];
	}

	public List<String> fields() {
		return new AbstractList<>() {
			@Override
			public String get(int index) {
				return fields[index];
			}

			@Override
			public int size() {
				return fields.length;
			}
		};
	}

	@Override
	public String toString() {
		return String.join("\t", fields);
	}
}
