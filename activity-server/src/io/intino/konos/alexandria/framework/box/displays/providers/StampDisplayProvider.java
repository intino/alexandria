package io.intino.konos.alexandria.framework.box.displays.providers;

import io.intino.konos.alexandria.framework.box.displays.AlexandriaStampDisplay;
import io.intino.konos.alexandria.framework.box.displays.AlexandriaTemporalStampDisplay;

public interface StampDisplayProvider {
	AlexandriaStampDisplay display(String stamp);
	AlexandriaTemporalStampDisplay temporalDisplay(String stamp);
}
