package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

public abstract class Icon<O> extends Stamp<O> {
	private Value<String> title;

	public String title(Item item, String username) {
		return objectTitle(item != null ? item.object() : null, username);
	}

	public String objectTitle(Object object, String username) {
		return title != null ? title.value(object, username) : "";
	}

	public Icon title(Value<String> title) {
		this.title = title;
		return this;
	}

}
