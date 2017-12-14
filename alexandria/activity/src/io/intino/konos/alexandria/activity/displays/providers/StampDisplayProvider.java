package io.intino.konos.alexandria.activity.displays.providers;

import io.intino.konos.alexandria.activity.displays.AlexandriaStamp;
import io.intino.konos.alexandria.activity.displays.AlexandriaTemporalStamp;

public interface StampDisplayProvider {
	AlexandriaStamp display(String stamp);
	AlexandriaTemporalStamp temporalDisplay(String stamp);
}
