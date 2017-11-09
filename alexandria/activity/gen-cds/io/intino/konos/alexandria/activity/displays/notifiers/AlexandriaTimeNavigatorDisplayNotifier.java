package io.intino.konos.alexandria.activity.displays.notifiers;

import io.intino.konos.alexandria.activity.schemas.*;

public class AlexandriaTimeNavigatorDisplayNotifier extends io.intino.konos.alexandria.activity.displays.AlexandriaDisplayNotifier {

    public AlexandriaTimeNavigatorDisplayNotifier(io.intino.konos.alexandria.activity.displays.AlexandriaDisplay display, io.intino.konos.alexandria.activity.displays.MessageCarrier carrier) {
        super(display, carrier);
    }

	public void refreshScales(java.util.List<Scale> value) {
		putToDisplay("refreshScales", "value", value);
	}

	public void refreshScale(String value) {
		putToDisplay("refreshScale", "value", value);
	}

	public void refreshOlapRange(Range value) {
		putToDisplay("refreshOlapRange", "value", value);
	}

	public void refreshDate(java.time.Instant value) {
		putToDisplay("refreshDate", "value", value);
	}

	public void refreshState(DateNavigatorState value) {
		putToDisplay("refreshState", "value", value);
	}
}
