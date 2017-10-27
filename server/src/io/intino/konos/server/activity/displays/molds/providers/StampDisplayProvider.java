package io.intino.konos.server.activity.displays.molds.providers;

import io.intino.konos.server.activity.displays.molds.StampDisplay;
import io.intino.konos.server.activity.displays.molds.TemporalStampDisplay;

public interface StampDisplayProvider {
	StampDisplay display(String stamp);
	TemporalStampDisplay temporalDisplay(String stamp);
}
