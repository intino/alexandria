package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;

public class ChangeItemEvent extends ItemEvent {
	public ChangeItemEvent(Display sender, Component component, Object item, int index) {
		super(sender, component, item, index);
	}
}
