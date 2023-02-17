package io.intino.alexandria.event.tuple;

import io.intino.alexandria.event.Event;

import java.time.Instant;

public class TupleEvent implements Event {

	private final String[] fields;

	public TupleEvent(String[] fields) {
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

	public String get(int index) {
		return fields[index];
	}

	@Override
	public String toString() {
		return String.join("\t", fields);
	}
}
