package io.intino.alexandria.ui.displays.components.slider.ordinals;

import io.intino.alexandria.Scale;
import io.intino.alexandria.ui.displays.components.slider.Ordinal;

public class HourOrdinal implements Ordinal {
	@Override
	public String name() {
		return Scale.Hour.name();
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
	public String formatter() {
		return null;
	}
}
