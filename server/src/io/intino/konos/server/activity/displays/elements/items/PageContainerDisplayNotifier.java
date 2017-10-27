package io.intino.konos.server.activity.displays.elements.items;

import io.intino.konos.server.activity.displays.schemas.PageLocation;

public class PageContainerDisplayNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

    public PageContainerDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshLocation(PageLocation value) {
		putToDisplay("refreshLocation", "value", value);
	}
}
