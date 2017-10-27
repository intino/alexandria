package io.intino.konos.server.activity.displays.panels;

import io.intino.konos.server.activity.displays.schemas.Reference;

public class PanelDisplayNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

    public PanelDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshTarget(String value) {
		putToDisplay("refreshTarget", "value", value);
	}

	// TODO Mario -> antes era itemList ahora referenceList
	public void refreshViewList(java.util.List<Reference> value) {
		putToDisplay("refreshViewList", "value", value);
	}

	public void refreshBreadcrumbs(String value) {
		putToDisplay("refreshBreadcrumbs", "value", value);
	}

	public void createPanel(String value) {
		putToDisplay("createPanel", "value", value);
	}

	public void showPanel() {
		putToDisplay("showPanel");
	}

	public void hidePanel() {
		putToDisplay("hidePanel");
	}

	public void showDialog() {
		putToDisplay("showDialog");
	}
}
