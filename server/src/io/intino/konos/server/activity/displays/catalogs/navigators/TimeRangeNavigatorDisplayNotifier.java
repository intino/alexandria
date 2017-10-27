package io.intino.konos.server.activity.displays.catalogs.navigators;

import io.intino.konos.server.activity.displays.schemas.Range;
import io.intino.konos.server.activity.displays.schemas.Scale;

public class TimeRangeNavigatorDisplayNotifier extends io.intino.konos.server.activity.displays.DisplayNotifier {

    public TimeRangeNavigatorDisplayNotifier(io.intino.konos.server.activity.displays.Display display, io.intino.konos.server.activity.displays.MessageCarrier carrier) {
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
