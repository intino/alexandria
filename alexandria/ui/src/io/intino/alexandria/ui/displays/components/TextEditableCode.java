package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;

public class TextEditableCode<B extends Box> extends AbstractTextEditableCode<B> {
	private String value;
	private ChangeListener changeListener = null;

	public TextEditableCode(B box) {
		super(box);
	}

	public String value() {
		return value;
	}

	public TextEditableCode<B> value(String value) {
		this.value = value.replaceAll("&#13;", "\n").replaceAll("\\\\n", "\n");
		return this;
	}

	public void update(String value) {
		this.value = value;
		notifier.refresh(value);
	}

	public TextEditableCode<B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public void notifyChange(String value) {
		this.value = value.replaceAll("&plus;", "+");
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, this.value));
	}
}