package io.intino.alexandria.event;

import java.time.Instant;

public interface Event extends Comparable<Event> {

	Instant ts();
	String ss();

	@Override
	default int compareTo(Event o) {
		return ts().compareTo(o.ts());
	}
}
