package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.TextEditableCodeNotifier;

public class TextEditableCode<DN extends TextEditableCodeNotifier, B extends Box> extends AbstractTextEditableCode<DN, B> {
	private String value;
	private ChangeListener changeListener = null;

	public TextEditableCode(B box) {
		super(box);
	}

	public String value() {
		return value;
	}

	public TextEditableCode<DN, B> value(String value) {
		this.value = value.replaceAll("&#13;", "\n").replaceAll("\\\\n", "\n");
		return this;
	}

	public void update(String value) {
		this.value = value;
		notifier.refresh(value);
	}

	public TextEditableCode<DN, B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public void notifyChange(String value) {
		this.value = value.replaceAll("&plus;", "+");
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, this.value));
	}
}