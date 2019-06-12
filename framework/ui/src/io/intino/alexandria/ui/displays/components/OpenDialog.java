package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.OpenDialogNotifier;

public class OpenDialog<DN extends OpenDialogNotifier, B extends Box> extends AbstractOpenDialog<DN, B> {
	private BaseDialog dialog;

	public OpenDialog(B box) {
        super(box);
    }

    public OpenDialog bindTo(BaseDialog dialog) {
    	this.dialog = dialog;
    	return this;
	}

	public void execute() {
		if (this.dialog == null) return;
		this.dialog.open();
	}
}