package io.intino.alexandria.ui.displays.components.slider.ordinals;

import io.intino.alexandria.Scale;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;

public class DayOrdinal implements Ordinal {
	@Override
	public String name() {
		return Scale.Day.name();
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
	public String formatter() {
		return null;
	}
}
