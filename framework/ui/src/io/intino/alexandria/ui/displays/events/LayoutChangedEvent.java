package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;

import java.util.List;

public class LayoutChangedEvent extends Event {
	private final List<Double> layout;
	private boolean cancel = false;

	public LayoutChangedEvent(Display sender, List<Double> layout) {
		super(sender);
		this.layout = layout;
	}

	public List<Double> layout() {
		return layout;
	}

}
