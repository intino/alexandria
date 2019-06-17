package io.intino.alexandria.ui.displays.components.slider;

public class Range {
	public long min;
	public long max;

	public long min() {
		return min;
	}

	public Range min(long min) {
		this.min = min;
		return this;
	}

	public long max() {
		return max;
	}

	public Range max(long max) {
		this.max = max;
		return this;
	}
}
