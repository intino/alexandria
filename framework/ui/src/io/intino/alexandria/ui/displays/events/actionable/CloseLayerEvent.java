package io.intino.alexandria.ui.displays.events.actionable;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.Layer;
import io.intino.alexandria.ui.displays.events.Event;

public class CloseLayerEvent extends Event {
	private final Layer<?, ?> layer;

	public CloseLayerEvent(Display<?, ?> sender, Layer<?, ?> layer) {
		super(sender);
		this.layer = layer;
	}

	public Layer<?, ?> layer() { return layer; }
}
