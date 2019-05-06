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
	private ChangeListener changeListener = null;

    public DateEditable(B box) {
        super(box);
    }

	public Instant value() {
		return value;
	}

	public DateEditable<DN, B> value(Instant value) {
		this.value = value;
		return this;
	}

	public Instant min() {
		return this.min;
	}

	public DateEditable<DN, B> min(Instant min) {
		this.min = min;
		return this;
	}

	public Instant max() {
		return this.max;
	}

	public DateEditable<DN, B> max(Instant max) {
		this.max = max;
		return this;
	}

	public void update(Instant value) {
		value(value);
		notifier.refresh(value);
	}

	public DateEditable<DN, B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public void notifyChange(Instant value) {
    	value(value);
    	if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
    }

}