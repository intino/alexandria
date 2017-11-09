package io.intino.konos.alexandria.activity.displays.providers;

import io.intino.konos.alexandria.activity.displays.AlexandriaStampDisplay;
import io.intino.konos.alexandria.activity.displays.AlexandriaTemporalStampDisplay;

public interface StampDisplayProvider {
	AlexandriaStampDisplay display(String stamp);
	AlexandriaTemporalStampDisplay temporalDisplay(String stamp);
}
