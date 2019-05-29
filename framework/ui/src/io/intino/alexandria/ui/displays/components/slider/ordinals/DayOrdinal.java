package io.intino.alexandria.ui.displays.components.slider.ordinals;

import io.intino.alexandria.ui.displays.components.slider.Ordinal;

public class DayOrdinal implements Ordinal {
	@Override
	public String label() {
		return "Day";
	}

	@Override
	public int step() {
		return 525600; // minutes
	}

	@Override
	public String formatter() {
		return null;
	}
}
