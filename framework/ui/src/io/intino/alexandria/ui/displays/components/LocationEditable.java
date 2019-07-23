package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.LocationEditableNotifier;

public class LocationEditable<DN extends LocationEditableNotifier, B extends Box> extends AbstractLocationEditable<DN, B> {
	private ChangeListener changeListener = null;

    public LocationEditable(B box) {
        super(box);
    }

	public LocationEditable<DN, B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public void notifyChange(String value) {
		value(value);
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value()));
	}

}