package io.intino.konos.server.activity.displays.molds.model.stamps;

import io.intino.konos.server.activity.displays.elements.model.Item;
import io.intino.konos.server.activity.displays.molds.model.Stamp;

public class Highlight extends Stamp<String> {
	private Value<String> color;

	public String color(Item item, String username) {
		if (item == null) return "";
		return color(item.object(), username);
	}

	public String color(Object object, String username) {
		return color != null ? color.value(object, username) : "";
	}

	public Highlight color(Value<String> color) {
		this.color = color;
		return this;
	}

	@Override
	public String objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
