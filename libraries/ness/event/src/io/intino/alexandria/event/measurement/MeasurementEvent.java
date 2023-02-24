package io.intino.alexandria.event.measurement;

import io.intino.alexandria.event.Event;

import java.time.Instant;

public class MeasurementEvent implements Event {
	private final Instant ts;
	private final String sensor;
	private final String[] measurements;
	private final double[] values;

	public MeasurementEvent(Instant ts, String sensor, String[] measurements, double[] values) {
		this.ts = ts;
		this.sensor = sensor;
		this.measurements = measurements;
		this.values = values;
	}

	@Override
	public Instant ts() {
		return ts;
	}

	@Override
	public String ss() {
		return sensor;
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