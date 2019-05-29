package io.intino.alexandria.ui.displays.components.slider.ordinals;

import io.intino.alexandria.ui.displays.components.slider.Ordinal;

public class YearOrdinal implements Ordinal {
	@Override
	public String label() {
		return "Year";
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
