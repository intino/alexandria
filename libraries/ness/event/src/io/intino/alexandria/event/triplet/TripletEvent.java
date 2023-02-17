package io.intino.alexandria.event.triplet;

import io.intino.alexandria.event.Event;

import java.time.Instant;

public class TripletEvent implements Event {

	String[] fields;

	public TripletEvent(String[] fields) {
		this.fields = fields;
	}

	public String subject() {
		return fields[0];
	}

	public String verb() {
		return fields[1];
	}

	public String object() {
		return fields[2];
	}

	public String author() {
		return fields.length > 3 ? fields[3] : null;
	}

	public String get(int index) {
		return fields[index];
	}

	@Override
	public String toString() {
		return String.join("\t", fields);
	}

	@Override
	public Instant ts() {
		return null;
	}

	@Override
	public String ss() {
		return null;
	}
}
