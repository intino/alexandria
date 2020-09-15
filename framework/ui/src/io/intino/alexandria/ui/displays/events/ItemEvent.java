package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;

public class ItemEvent extends Event {
	private final Component component;
	private final Object item;
	private final int index;

	public ItemEvent(Display sender, Component component, Object item, int index) {
		super(sender);
		this.component = component;
		this.item = item;
		this.index = index;
	}

	public <Item> Item item() {
		return (Item) item;
	}

	public int index() {
		return index;
	}

	public <C extends Component> C component() {
		return (C) component;
	}

}
