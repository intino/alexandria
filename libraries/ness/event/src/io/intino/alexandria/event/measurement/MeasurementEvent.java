package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.Event;

import java.time.Instant;

public class MeasurementEvent implements Event {

	// TODO

	@Override
	public Instant ts() {
		return null;
	}

	@Override
	public String ss() {
		return null;
	}

	@Override
	public Format format() {
		return Format.Measurement;
	}
}
