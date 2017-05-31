package io.intino.konos.server.activity.dialogs;

import io.intino.konos.server.activity.dialogs.schemas.Dialog;
import io.intino.konos.server.activity.dialogs.schemas.Validation;

public class DialogNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

    public DialogNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void render(Dialog value) {
		putToDisplay("render", "value", value);
	}

	public void refresh(Validation value) {
		putToDisplay("refresh", "value", value);
	}
}

