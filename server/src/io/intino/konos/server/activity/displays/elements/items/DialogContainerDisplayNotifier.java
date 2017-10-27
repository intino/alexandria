package io.intino.konos.server.activity.displays.elements.items;

import io.intino.konos.server.activity.displays.schemas.DialogReference;

public class DialogContainerDisplayNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

    public DialogContainerDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshDialog(DialogReference value) {
		putToDisplay("refreshDialog", "value", value);
	}
}
