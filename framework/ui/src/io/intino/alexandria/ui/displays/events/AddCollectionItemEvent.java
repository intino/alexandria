package io.intino.alexandria.ui.displays.events;

import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.CollectionItemComponent;

public class AddCollectionItemEvent extends ItemEvent<CollectionItemComponent> {

	public AddCollectionItemEvent(Display<?, ?> sender, CollectionItemComponent component, Object item, int index) {
		super(sender, component, item, index);
	}

}
