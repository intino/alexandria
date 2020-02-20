package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.actionable.CloseListener;
import io.intino.alexandria.ui.displays.notifiers.CloseDialogNotifier;

public class CloseDialog<DN extends CloseDialogNotifier, B extends Box> extends AbstractCloseDialog<DN, B> {
	private BaseDialog dialog;
	private CloseListener closeListener;

	public CloseDialog(B box) {
		super(box);
	}

	public CloseDialog onClose(CloseListener listener) {
		this.closeListener = listener;
		return this;
	}

	public CloseDialog bindTo(BaseDialog dialog) {
		this.dialog = dialog;
		return this;
	}

	public void execute() {
		if (this.dialog == null) return;
		this.dialog.close();
		if (closeListener != null) closeListener.accept(new Event(this));
	}
}