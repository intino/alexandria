package io.intino.konos.alexandria.activity.displays.notifiers;

public class AlexandriaDesktopDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaDesktopDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void displayType(String value) {
		putToDisplay("displayType", "value", value);
	}

	public void loading(Boolean value) {
		put("loading", "value", value);
	}

	public void loaded() {
		put("loaded");
	}
}
