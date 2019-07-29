package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.CloseDialogNotifier;

public class CloseDialog<DN extends CloseDialogNotifier, B extends Box> extends AbstractCloseDialog<DN, B> {
	private BaseDialog dialog;

	public CloseDialog(B box) {
		super(box);
	}

	public CloseDialog bindTo(BaseDialog dialog) {
		this.dialog = dialog;
		return this;
	}

	public void execute() {
		if (this.dialog == null) return;
		this.dialog.close();
	}
}