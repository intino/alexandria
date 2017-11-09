package io.intino.konos.alexandria.activity.displays.notifiers;

import io.intino.konos.alexandria.activity.schemas.*;

public class AlexandriaPanelDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaPanelDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshTarget(String value) {
		putToDisplay("refreshTarget", "value", value);
	}

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
