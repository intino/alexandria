package io.intino.konos.server.activity.displays.schemas;

public class Range implements java.io.Serializable {

	private double min = 0.0;
	private double max = 0.0;
	private String scale = "";

	public double min() {
		return this.min;
	}

	public double max() {
		return this.max;
	}

	public String scale() {
		return this.scale;
	}

	public Range min(double min) {
		this.min = min;
		return this;
	}

	public Range max(double max) {
		this.max = max;
		return this;
	}

	public Range scale(String scale) {
		this.scale = scale;
		return this;
	}
}