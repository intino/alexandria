package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.Range;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.DateEditableNotifier;

import javax.swing.text.View;
import java.time.Instant;
import java.util.stream.Collectors;

public class DateEditable<DN extends DateEditableNotifier, B extends Box> extends AbstractDateEditable<DN, B> implements Editable<DN, B> {
	private Instant min;
	private Instant max;
	private Instant value;
	private boolean readonly;
	private ChangeListener changeListener = null;

	public enum View {
		Year, Date, Month;
	}

    public DateEditable(B box) {
        super(box);
    }

	public Instant value() {
		return value;
	}

	public Instant min() {
		return this.min;
	}

	public DateEditable<DN, B> min(Instant min) {
		_min(min);
		notifier.refreshRange(range());
		return this;
	}

	public Instant max() {
		return this.max;
	}

	public DateEditable<DN, B> max(Instant max) {
		_max(max);
		notifier.refreshRange(range());
		return this;
	}

	public DateEditable<DN, B> range(Instant min, Instant max) {
		_min(min);
		_max(max);
		notifier.refreshRange(range());
		return this;
	}

	public DateEditable<DN, B> pattern(String pattern) {
		notifier.refreshPattern(pattern);
		return this;
	}

	public DateEditable<DN, B> views(java.util.List<DateEditable.View> views) {
		notifier.refreshViews(views.stream().map(Enum::name).collect(Collectors.toList()));
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
	public DateEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifier.refreshReadonly(readonly);
		return this;
	}

	public void value(Instant value) {
		_value(value);
		notifier.refresh(value);
	}

	@Override
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

	private Range range() {
		Range range = new Range();
		if (min != null) range.min(min.toEpochMilli());
		if (max != null) range.max(max.toEpochMilli());
		return range;
	}

}