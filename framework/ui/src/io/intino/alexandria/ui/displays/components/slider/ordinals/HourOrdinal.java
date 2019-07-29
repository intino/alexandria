package io.intino.alexandria.ui.displays.components.slider.ordinals;

import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.model.TimeScale;

import java.time.Instant;

public class HourOrdinal implements Ordinal {
	@Override
	public String name() {
		return TimeScale.Hour.name();
	}

	@Override
	public String label() {
		return "Hour";
	}

	@Override
	public int step() {
		return 1;
	}

	@Override
	public Formatter formatter() {
		return value -> TimeScale.Hour.toString(Instant.ofEpochMilli(value)) + ":00";
	}
}
