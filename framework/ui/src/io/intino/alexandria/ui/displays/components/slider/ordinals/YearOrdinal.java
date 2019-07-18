package io.intino.alexandria.ui.displays.components.slider.ordinals;

import io.intino.alexandria.Scale;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;
import io.intino.alexandria.ui.model.TimeScale;

import java.time.Instant;

public class YearOrdinal implements Ordinal {
	@Override
	public String name() {
		return Scale.Year.name();
	}

	@Override
	public String label() {
		return "Year";
	}

	@Override
	public int step() {
		return 1;
	}

	@Override
	public Formatter formatter() {
		return value -> TimeScale.Year.toString(Instant.ofEpochMilli(value));
	}
}
