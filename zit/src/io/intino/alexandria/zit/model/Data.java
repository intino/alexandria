package io.intino.alexandria.zit.model;

import java.time.Instant;

public class Data {

	private final Instant ts;
	private final double[] values;
	private String[] model;

	public Data(Instant ts, double[] values, String[] model) {
		this.ts = ts;
		this.values = values;
		this.model = model;
	}

	public Instant ts() {
		return ts;
	}

	public double[] values() {
		return values;
	}

	public String[] model() {
		return model;
	}
}
