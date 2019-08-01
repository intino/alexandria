package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.NumberEditableNotifier;

public class NumberEditable<DN extends NumberEditableNotifier, B extends Box> extends AbstractNumberEditable<DN, B> {
	private double value;
	private double min;
	private double max;
	private double step;
	private boolean readonly;
	private ChangeListener changeListener = null;

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

	public boolean readonly() {
		return readonly;
	}

	public NumberEditable<DN, B> readonly(boolean value) {
		this.readonly = readonly;
		return this;
	}

	public void update(double value) {
		value(value);
		notifier.refresh(value);
	}

	public NumberEditable<DN, B> updateReadonly(boolean value) {
		readonly(value);
		notifier.refreshReadonly(value);
		return this;
	}

	public NumberEditable<DN, B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public void notifyChange(Double value) {
		this.value = value;
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
	}

}