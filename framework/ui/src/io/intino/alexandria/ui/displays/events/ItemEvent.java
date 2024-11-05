package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.Display;

public class ItemEvent<C extends Component> extends Event {
	private final C component;
	private final Object item;
	private final int index;

	public ItemEvent(Display<?, ?> sender, C component, Object item, int index) {
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

	public <T extends C> T component() {
		return (T) component;
	}

}
