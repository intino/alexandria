package io.intino.konos.server.activity.displays.layouts;

import io.intino.konos.server.activity.displays.schemas.PlatformInfo;
import io.intino.konos.server.activity.displays.schemas.Reference;
import io.intino.konos.server.activity.displays.schemas.UserInfo;

public class LayoutDisplayNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

    public LayoutDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
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
