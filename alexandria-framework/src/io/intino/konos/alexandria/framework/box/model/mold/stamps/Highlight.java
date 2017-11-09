package io.intino.konos.alexandria.framework.box.model.mold.stamps;

import io.intino.konos.alexandria.framework.box.model.Item;
import io.intino.konos.alexandria.framework.box.model.mold.Stamp;

public class Highlight extends Stamp<String> {
	private Value<String> color;

	public String color(Item item, String username) {
		if (item == null) return "";
		return objectColor(item.object(), username);
	}

	public String objectColor(Object object, String username) {
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
