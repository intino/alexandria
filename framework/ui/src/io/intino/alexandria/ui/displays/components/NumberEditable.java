package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.NumberEditableRange;
import io.intino.alexandria.schemas.Range;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.NumberEditableNotifier;

import java.time.Instant;

public class NumberEditable<DN extends NumberEditableNotifier, B extends Box> extends AbstractNumberEditable<DN, B> implements Editable<DN, B> {
	private Double value;
	private Double min;
	private Double max;
	private Double step;
	private boolean readonly;
	private ChangeListener beforeChangeListener = null;
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

	public NumberEditable<DN, B> min(Double min) {
		_min(min);
		notifier.refreshRange(range());
		return this;
	}

	public Double max() {
		return max;
	}

	public NumberEditable<DN, B> max(Double max) {
		_max(max);
		notifier.refreshRange(range());
		return this;
	}

	public Double step() {
		return step;
	}

	public NumberEditable<DN, B> range(Double min, Double max) {
		_min(min);
		_max(max);
		notifier.refreshRange(range());
		return this;
	}

	@Override
	public boolean readonly() {
		return readonly;
	}

	@Override
	public void reload() {
		notifier.refresh(value());
	}

	@Override
	public NumberEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifier.refreshReadonly(readonly);
		return this;
	}

	public NumberEditable value(double value) {
    	return value(Double.valueOf(value));
	}

	public NumberEditable value(Double value) {
		_value(value);
		notifier.refresh(value);
		return this;
	}

	public NumberEditable<DN, B> onBeforeChange(ChangeListener listener) {
		this.beforeChangeListener = listener;
		return this;
	}

	@Override
	public NumberEditable<DN, B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public void notifyChange(Double value) {
    	if (!checkRange(value)) {
    		notifier.refreshError(String.format(this.translate("Value out of range: %.1f-%.1f"), min != -1 ? min : Integer.MIN_VALUE, max != -1 ? max : Integer.MAX_VALUE));
    		return;
		}
		notifier.refreshError(null);
		if (beforeChangeListener != null) {
			ChangeEvent event = new ChangeEvent(this, value);
			beforeChangeListener.accept(event);
			if (event.cancel()) {
				notifier.refresh(this.value);
				return;
			}
		}
		this.value = value;
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
	}

	private boolean checkRange(Double value) {
		if (min != null && min != -1 && value < min) return false;
		if (max != null && max != -1 && value > max) return false;
		return true;
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

	private NumberEditableRange range() {
		NumberEditableRange range = new NumberEditableRange();
		if (min != null) range.min(min);
		if (max != null) range.max(max);
		return range;
	}

}