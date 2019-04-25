package io.intino.alexandria.ui.displays;

import io.intino.alexandria.rest.pushservice.MessageCarrier;
import io.intino.alexandria.ui.schemas.Dialog;
import io.intino.alexandria.ui.schemas.Validation;

public class AlexandriaDialogNotifier extends AlexandriaDisplayNotifier {

    public AlexandriaDialogNotifier(AlexandriaDisplay display, MessageCarrier carrier) {
        super(display, carrier);
    }

	public void render(Dialog value) {
		putToDisplay("render", "value", value);
	}

	public void refresh(Validation value) {
		putToDisplay("refresh", "value", value);
	}

	public void notifyUser(String value) {
		putToDisplay("notifyUser", "value", value);
	}

	public void upload(io.intino.alexandria.Resource value) {
		putToDisplay("upload", "value", value);
	}

	public void done(String value) {
		putToDisplay("done", "value", value);
	}

}
