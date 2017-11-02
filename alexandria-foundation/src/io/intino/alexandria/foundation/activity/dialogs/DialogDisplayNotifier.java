package io.intino.alexandria.foundation.activity.dialogs;

import io.intino.alexandria.foundation.activity.schemas.Dialog;
import io.intino.alexandria.foundation.activity.schemas.Validation;

public class DialogDisplayNotifier extends io.intino.alexandria.foundation.activity.displays.DisplayNotifier {

    public DialogDisplayNotifier(io.intino.alexandria.foundation.activity.displays.Display display, io.intino.alexandria.foundation.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void render(Dialog value) {
		putToDisplay("render", "value", value);
	}

	public void refresh(Validation value) {
		putToDisplay("refresh", "value", value);
	}

	public void upload(io.intino.alexandria.Resource value) {
		putToDisplay("upload", "value", value);
	}

	public void done(String value) {
		putToDisplay("done", "value", value);
	}
}
