package io.intino.alexandria.ui.displays.events.collection;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;

import java.util.List;

public class RefreshEvent extends Event {
	private final List<Object> items;

	public RefreshEvent(Display sender, List<Object> items) {
		super(sender);
		this.items = items;
	}

	public <Item> List<Item> items() {
		return (List<Item>) items;
	}

}
