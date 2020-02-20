package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.actionable.OpenListener;
import io.intino.alexandria.ui.displays.notifiers.OpenDialogNotifier;

public class OpenDialog<DN extends OpenDialogNotifier, B extends Box> extends AbstractOpenDialog<DN, B> {
	private BaseDialog dialog;
	private OpenListener openListener = null;

	public OpenDialog(B box) {
        super(box);
    }

	public OpenDialog onOpen(OpenListener listener) {
		this.openListener = listener;
		return this;
	}

	public OpenDialog bindTo(BaseDialog dialog) {
    	this.dialog = dialog;
    	return this;
	}

	public void execute() {
		if (this.dialog == null) return;
		this.dialog.open();
		if (openListener != null) openListener.accept(new Event(this));
	}
}