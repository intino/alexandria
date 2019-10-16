package io.intino.alexandria.ui.displays.components.slider.ordinals;

import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.model.TimeScale;

import java.time.Instant;

public class DayOrdinal implements Ordinal {
	@Override
	public String name() {
		return TimeScale.Day.name();
	}

	@Override
	public String label() {
		return "Day";
	}

	@Override
	public int step() {
		return 1;
	}

	@Override
	public Formatter formatter(String language) {
		return value -> TimeScale.Day.toString(Instant.ofEpochMilli(value), language);
	}
}
