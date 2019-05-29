package io.intino.alexandria.ui.displays.components.slider;

public class Range {
	public int min;
	public int max;

	public int min() {
		return min;
	}

	public Range min(int min) {
		this.min = min;
		return this;
	}

	public int max() {
		return max;
	}

	public Range max(int max) {
		this.max = max;
		return this;
	}
}
