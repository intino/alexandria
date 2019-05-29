package io.intino.alexandria.ui.displays.components.slider.ordinals;

import io.intino.alexandria.ui.displays.components.slider.Ordinal;

public class MinuteOrdinal implements Ordinal {
	@Override
	public String label() {
		return "Minute";
	}

	@Override
	public int step() {
		return -1;
	}

	@Override
	public String formatter() {
		return null;
	}
}
