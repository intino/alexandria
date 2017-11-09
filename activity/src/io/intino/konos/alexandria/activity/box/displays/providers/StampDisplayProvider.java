package io.intino.konos.alexandria.activity.box.displays.providers;

import io.intino.konos.alexandria.activity.box.displays.AlexandriaStampDisplay;
import io.intino.konos.alexandria.activity.box.displays.AlexandriaTemporalStampDisplay;

public interface StampDisplayProvider {
	AlexandriaStampDisplay display(String stamp);
	AlexandriaTemporalStampDisplay temporalDisplay(String stamp);
}
