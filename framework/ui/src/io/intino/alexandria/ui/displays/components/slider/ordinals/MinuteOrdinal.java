package io.intino.alexandria.ui.displays.components.slider.ordinals;

import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.model.TimeScale;

import java.time.Instant;

public class MinuteOrdinal implements Ordinal {
	@Override
	public String name() {
		return TimeScale.Minute.name();
	}

	@Override
	public String label() {
		return "Minute";
	}

	@Override
	public int step() {
		return 1;
	}

	@Override
	public Formatter formatter() {
		return value -> TimeScale.Minute.toString(Instant.ofEpochMilli(value));
	}
}
