package io.intino.alexandria.itz;

import java.time.Instant;

public class Data {

	private final Instant ts;
	private final double[] values;

	public Data(Instant ts, double[] values) {
		this.ts = ts;
		this.values = values;
	}

	public Instant ts() {
		return ts;
	}

	public double[] values() {
		return values;
	}
}
