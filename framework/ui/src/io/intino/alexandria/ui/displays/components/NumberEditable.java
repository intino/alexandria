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

	public double min() {
		return min;
	}

	public double max() {
		return max;
	}

	public double step() {
		return step;
	}

	public boolean readonly() {
		return readonly;
	}

	public void update(double value) {
		_value(value);
		notifier.refresh(value);
	}

	public NumberEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifier.refreshReadonly(readonly);
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

	protected NumberEditable _value(double value) {
		this.value = value;
		return this;
	}

	protected NumberEditable _min(double min) {
		this.min = min;
		return this;
	}

	protected NumberEditable _max(double max) {
		this.max = max;
		return this;
	}

	protected NumberEditable _step(double step) {
		this.step = step;
		return this;
	}

	protected NumberEditable<DN, B> _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

}