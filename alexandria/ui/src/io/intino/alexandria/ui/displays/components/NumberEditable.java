package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

public class NumberEditable<B extends Box> extends AbstractNumberEditable<B> {
	private double value;
	private double min;
	private double max;
	private double step;

    public NumberEditable(B box) {
        super(box);
    }

	public double value() {
		return value;
	}

	public NumberEditable value(double value) {
		this.value = value;
		return this;
	}

	public double min() {
		return min;
	}

	public NumberEditable min(double min) {
		this.min = min;
		return this;
	}

	public double max() {
		return max;
	}

	public NumberEditable max(double max) {
		this.max = max;
		return this;
	}

	public double step() {
		return step;
	}

	public NumberEditable step(double step) {
		this.step = step;
		return this;
	}

	public void update(double value) {
		value(value);
		notifier.refresh(value);
	}

	public void notifyChange(Double value) {
	}

}