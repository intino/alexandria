package io.intino.konos.server.activity.displays.catalogs.navigators;

import io.intino.konos.server.activity.displays.schemas.DateNavigatorState;
import io.intino.konos.server.activity.displays.schemas.Range;
import io.intino.konos.server.activity.displays.schemas.Scale;

public class TimeNavigatorDisplayNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

    public TimeNavigatorDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
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
