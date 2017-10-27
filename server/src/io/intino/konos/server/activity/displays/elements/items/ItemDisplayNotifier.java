package io.intino.konos.server.activity.displays.elements.items;

import io.intino.konos.server.activity.displays.schemas.ItemRefreshInfo;

public class ItemDisplayNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

    public ItemDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refresh(ItemRefreshInfo value) {
		putToDisplay("refresh", "value", value);
	}

	public void refreshMode(String value) {
		putToDisplay("refreshMode", "value", value);
	}

	public void refreshEmptyMessage(String value) {
		putToDisplay("refreshEmptyMessage", "value", value);
	}
}
