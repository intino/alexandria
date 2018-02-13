package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class Highlight extends Stamp<String> {
	private Value<String> color;

	public String color(Item item, ActivitySession session) {
		return objectColor(item != null ? item.object() : null, session);
	}

	public String objectColor(Object object, ActivitySession session) {
		return color != null ? color.value(object, session) : "";
	}

	public Highlight color(Value<String> color) {
		this.color = color;
		return this;
	}

	@Override
	public String objectValue(Object object, ActivitySession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
