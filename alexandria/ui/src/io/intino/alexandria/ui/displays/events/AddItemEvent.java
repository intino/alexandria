package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;

public class AddItemEvent extends Event {
	private final Component component;
	private final Object item;

	public AddItemEvent(Display sender, Component component, Object item) {
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
