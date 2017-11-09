package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.activity.box.displays.AlexandriaDisplay;
import io.intino.konos.alexandria.activity.box.displays.AlexandriaDisplayNotifier;
import io.intino.konos.alexandria.activity.box.schemas.Dialog;
import io.intino.konos.alexandria.activity.box.schemas.Validation;

public class AlexandriaDialogDisplayNotifier extends AlexandriaDisplayNotifier {

    public AlexandriaDialogDisplayNotifier(AlexandriaDisplay display, io.intino.konos.alexandria.activity.box.displays.MessageCarrier carrier) {
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
