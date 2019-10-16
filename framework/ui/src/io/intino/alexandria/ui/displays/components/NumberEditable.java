package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.NumberEditableNotifier;

public class NumberEditable<DN extends NumberEditableNotifier, B extends Box> extends AbstractNumberEditable<DN, B> {
	private Double value;
	private Double min;
	private Double max;
	private Double step;
	private boolean readonly;
	private ChangeListener changeListener = null;

    public NumberEditable(B box) {
        super(box);
    }

	public Double value() {
		return value;
	}

	public Double min() {
		return min;
	}

	public Double max() {
		return max;
	}

	public Double step() {
		return step;
	}

	public boolean readonly() {
		return readonly;
	}

	public NumberEditable value(double value) {
    	return value(Double.valueOf(value));
	}

	public NumberEditable value(Double value) {
		_value(value);
		notifier.refresh(value);
		return this;
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
		return _value(Double.valueOf(value));
	}

	protected NumberEditable _value(Double value) {
		this.value = value;
		return this;
	}

	protected NumberEditable _min(double value) {
		return _min(Double.valueOf(value));
	}

	protected NumberEditable _min(Double min) {
		this.min = min;
		return this;
	}

	protected NumberEditable _max(double value) {
		return _max(Double.valueOf(value));
	}

	protected NumberEditable _max(Double max) {
		this.max = max;
		return this;
	}

	protected NumberEditable _step(double value) {
		return _step(Double.valueOf(value));
	}

	protected NumberEditable _step(Double step) {
		this.step = step;
		return this;
	}

	protected NumberEditable<DN, B> _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

}