package io.intino.konos.alexandria.foundation.activity.dialogs;

import io.intino.konos.alexandria.foundation.activity.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.foundation.activity.displays.AlexandriaDisplayNotifier;
import io.intino.konos.alexandria.foundation.activity.schemas.Dialog;
import io.intino.konos.alexandria.foundation.activity.schemas.Validation;

public class AlexandriaDialogDisplayNotifier extends AlexandriaDisplayNotifier {

    public AlexandriaDialogDisplayNotifier(AlexandriaDisplay display, io.intino.konos.alexandria.foundation.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void render(Dialog value) {
		putToDisplay("render", "value", value);
	}

	public void refresh(Validation value) {
		putToDisplay("refresh", "value", value);
	}

	public void upload(io.intino.konos.alexandria.Resource value) {
		putToDisplay("upload", "value", value);
	}

	public void done(String value) {
		putToDisplay("done", "value", value);
	}
}
