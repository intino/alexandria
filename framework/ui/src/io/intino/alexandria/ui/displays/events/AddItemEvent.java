package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;

public class AddItemEvent extends ItemEvent {
	public AddItemEvent(Display sender, Component component, Object item, int index) {
		super(sender, component, item, index);
	}
}
