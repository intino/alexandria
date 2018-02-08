package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.User;

public class Highlight extends Stamp<String> {
	private Value<String> color;

	public String color(Item item, User user) {
		return objectColor(item != null ? item.object() : null, user);
	}

	public String objectColor(Object object, User user) {
		return color != null ? color.value(object, user) : "";
	}

	public Highlight color(Value<String> color) {
		this.color = color;
		return this;
	}

	@Override
	public String objectValue(Object object, User user) {
		return value() != null ? value().value(object, user) : null;
	}

}
