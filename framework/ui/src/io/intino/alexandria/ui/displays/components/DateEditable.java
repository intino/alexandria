package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.Range;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.events.ReadonlyEvent;
import io.intino.alexandria.ui.displays.events.ReadonlyListener;
import io.intino.alexandria.ui.displays.notifiers.DateEditableNotifier;

import java.time.Instant;
import java.util.stream.Collectors;

public class DateEditable<DN extends DateEditableNotifier, B extends Box> extends AbstractDateEditable<DN, B> implements Editable<DN, B> {
	private Instant min;
	private Instant max;
	private Instant value;
	private String pattern;
	private boolean readonly;
	private ChangeListener changeListener = null;
	private ReadonlyListener readonlyListener = null;

	public enum View {
		Year, Month, Week, Date;
	}

    public DateEditable(B box) {
        super(box);
    }

	@Override
	public void didMount() {
		super.didMount();
		notifier.refreshRange(range());
		if (pattern != null) notifier.refreshPattern(pattern);
		if (value != null) notifier.refresh(value);
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
		this.pattern = pattern;
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
	public DateEditable<DN, B> focus() {
		notifier.refreshFocused(true);
		return this;
	}

	@Override
	public DateEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifyReadonly(readonly);
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

	@Override
	public DateEditable<DN, B> onReadonly(ReadonlyListener listener) {
		this.readonlyListener = listener;
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

	private void notifyReadonly(boolean value) {
		if (readonlyListener != null) readonlyListener.accept(new ReadonlyEvent(this, value));
		notifier.refreshReadonly(value);
	}

}