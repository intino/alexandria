package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.DateEditableNotifier;

import java.time.Instant;

public class DateEditable<DN extends DateEditableNotifier, B extends Box> extends AbstractDateEditable<DN, B> {
	private Instant min;
	private Instant max;
	private Instant value;
	private boolean readonly;
	private ChangeListener changeListener = null;

    public DateEditable(B box) {
        super(box);
    }

	public Instant value() {
		return value;
	}

	public Instant min() {
		return this.min;
	}

	public Instant max() {
		return this.max;
	}

	public boolean readonly() {
		return readonly;
	}

	public void value(Instant value) {
		_value(value);
		notifier.refresh(value);
	}

	public DateEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifier.refreshReadonly(readonly);
		return this;
	}

	public DateEditable<DN, B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public void notifyChange(Instant value) {
    	value(value);
    	if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
    }

	protected DateEditable<DN, B> _value(Instant value) {
		this.value = value;
		return this;
	}

	protected DateEditable<DN, B> _min(Instant min) {
		this.min = min;
		return this;
	}

	protected DateEditable<DN, B> _max(Instant max) {
		this.max = max;
		return this;
	}

	protected DateEditable<DN, B> _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

}