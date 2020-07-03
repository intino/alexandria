package io.intino.alexandria.ui.displays.events.collection;

import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.events.Event;

public class SelectItemEvent extends Event {
	private final Component component;
	private final Object item;

	public SelectItemEvent(Display sender, Component component, Object item) {
		super(sender);
		this.component = component;
		this.item = item;
	}

	public <Item> Item item() {
		return (Item) item;
	}

	public <C extends Component> C component() {
		return (C) component;
	}

}
