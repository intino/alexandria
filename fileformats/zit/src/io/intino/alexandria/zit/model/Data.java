package io.intino.alexandria.zit.model;

import java.time.Instant;

public class Data {

	private final Instant ts;
	private final double[] values;
	private final String[] measurements;

	public Data(Instant ts, double[] values, String[] measurements) {
		this.ts = ts;
		this.values = values;
		this.measurements = measurements;
	}

	public Instant ts() {
		return ts;
	}

	public double[] values() {
		return values;
	}

	public String[] measurements() {
		return measurements;
	}
}
