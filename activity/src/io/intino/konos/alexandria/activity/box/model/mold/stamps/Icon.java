package io.intino.konos.alexandria.activity.box.model.mold.stamps;

import io.intino.konos.alexandria.activity.box.model.Item;
import io.intino.konos.alexandria.activity.box.model.mold.Stamp;

public abstract class Icon<O> extends Stamp<O> {
	private Value<String> title;

	public String title(Item item, String username) {
		if (item == null) return "";
		return objectTitle(item.object(), username);
	}

	public String objectTitle(Object object, String username) {
		return title != null ? title.value(object, username) : "";
	}

	public Icon title(Value<String> title) {
		this.title = title;
		return this;
	}

}
