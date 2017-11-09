package io.intino.konos.alexandria.activity.displays.notifiers;

import io.intino.konos.alexandria.activity.schemas.*;

public class AlexandriaTabLayoutDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaTabLayoutDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void info(PlatformInfo value) {
		putToDisplay("info", "value", value);
	}

	public void refreshSelected(String value) {
		putToDisplay("refreshSelected", "value", value);
	}

	public void refreshItemList(java.util.List<Reference> value) {
		putToDisplay("refreshItemList", "value", value);
	}

	public void user(UserInfo value) {
		putToDisplay("user", "value", value);
	}

	public void userLoggedOut(String value) {
		putToDisplay("userLoggedOut", "value", value);
	}

	public void loading() {
		put("loading");
	}

	public void loaded() {
		put("loaded");
	}
}
