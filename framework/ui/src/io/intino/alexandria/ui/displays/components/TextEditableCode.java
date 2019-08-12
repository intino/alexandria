package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.TextEditableCodeNotifier;

public class TextEditableCode<DN extends TextEditableCodeNotifier, B extends Box> extends AbstractTextEditableCode<DN, B> {
	private ChangeListener changeListener = null;

	public TextEditableCode(B box) {
		super(box);
	}

	public TextEditableCode<DN, B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public void notifyChange(String value) {
		_value(value.replaceAll("&plus;", "+"));
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value()));
	}

	@Override
	protected TextEditableCode<DN, B> _value(String value) {
		super.value(value.replaceAll("&#13;", "\n").replaceAll("\\\\n", "\n"));
		return this;
	}
}