package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.events.ReadonlyEvent;
import io.intino.alexandria.ui.displays.events.ReadonlyListener;
import io.intino.alexandria.ui.displays.notifiers.TextEditableCodeNotifier;

public class TextEditableCode<DN extends TextEditableCodeNotifier, B extends Box> extends AbstractTextEditableCode<DN, B> implements Editable<DN, B> {
	private boolean readonly;
	private ChangeListener changeListener = null;
	private ReadonlyListener readonlyListener = null;

	public TextEditableCode(B box) {
		super(box);
	}

	public TextEditableCode<DN, B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	@Override
	public void reload() {
		notifier.refresh(value());
	}

	@Override
	public boolean readonly() {
		return readonly;
	}

	@Override
	public TextEditableCode<DN, B> onReadonly(ReadonlyListener listener) {
		this.readonlyListener = listener;
		return this;
	}

	@Override
	public TextEditableCode<DN, B> focus() {
		notifier.refreshFocused(true);
		return this;
	}

	public void update(String value) {
		if (!notifyChange(value)) return;
		super.value(value);
	}

	public boolean notifyChange(String value) {
		_value(value != null ? value.replaceAll("&plus;", "+") : null);
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value()));
		return true;
	}

	@Override
	public TextEditableCode<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifyReadonly(readonly);
		return this;
	}

	@Override
	protected TextEditableCode<DN, B> _value(String value) {
		super._value(value != null ? value.replaceAll("&#13;", "\n") : null);
		return this;
	}

	protected TextEditableCode<DN, B> _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	private void notifyReadonly(boolean value) {
		if (readonlyListener != null) readonlyListener.accept(new ReadonlyEvent(this, value));
		notifier.refreshReadonly(value);
	}

}