package io.intino.alexandria.ui.displays.events.actionable;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.Layer;
import io.intino.alexandria.ui.displays.events.Event;

public class CopyEvent extends Event {
	private final String text;

	public CopyEvent(Display<?, ?> sender, String text) {
		super(sender);
		this.text = text;
	}

	public String text() { return text; }
}
