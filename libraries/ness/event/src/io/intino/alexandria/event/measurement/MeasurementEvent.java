package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.Event;

import java.time.Instant;

public class MeasurementEvent implements Event {
	private String type;
	private final Instant ts;
	private final String source;
	private final String[] measurements;
	private final double[] values;

	public MeasurementEvent(String type, String source, Instant ts, String[] measurements, double[] values) {
		this.type = type;
		this.ts = ts;
		this.source = source;
		this.measurements = measurements;
		this.values = values;
	}

	@Override
	public String type() {
		return type;
	}

	@Override
	public Instant ts() {
		return ts;
	}

	@Override
	public String ss() {
		return source;
	}

	public String[] measurements() {
		return measurements;
	}

	public double[] values() {
		return values;
	}

	@Override
	public Format format() {
		return Format.Measurement;
	}
}