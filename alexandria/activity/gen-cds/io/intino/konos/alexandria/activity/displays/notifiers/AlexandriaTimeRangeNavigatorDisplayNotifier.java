package io.intino.konos.alexandria.activity.displays.notifiers;

import io.intino.konos.alexandria.activity.schemas.*;

public class AlexandriaTimeRangeNavigatorDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaTimeRangeNavigatorDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshScales(java.util.List<Scale> value) {
		putToDisplay("refreshScales", "value", value);
	}

	public void refreshOlapRange(Range value) {
		putToDisplay("refreshOlapRange", "value", value);
	}

	public void refreshZoomRange(Range value) {
		putToDisplay("refreshZoomRange", "value", value);
	}

	public void refreshRange(Range value) {
		putToDisplay("refreshRange", "value", value);
	}
}
