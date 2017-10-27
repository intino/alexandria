package io.intino.konos.server.activity.displays.molds.model.stamps;

import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.molds.model.Stamp;

public class ItemLinks extends Stamp<Links> {
	private Value<String> title;

	public String title(Item item, String username) {
		if (item == null) return null;
		return title(item.object(), username);
	}

	public String title(Object object, String username) {
		return title != null ? title.value(object, username) : null;
	}

	public ItemLinks title(Value<String> title) {
		this.title = title;
		return this;
	}

	@Override
	public Links value(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
