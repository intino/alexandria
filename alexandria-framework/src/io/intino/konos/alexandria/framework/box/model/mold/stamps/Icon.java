package io.intino.konos.alexandria.framework.box.model.mold.stamps;

import io.intino.konos.alexandria.framework.box.model.Item;
import io.intino.konos.alexandria.framework.box.model.mold.Stamp;

public abstract class Icon<O> extends Stamp<O> {
	private Value<String> title;

	public String title(Item item, String username) {
		if (item == null) return "";
		return title(item.object(), username);
	}

	public String title(Object object, String username) {
		return title != null ? title.value(object, username) : "";
	}

	public Icon title(Value<String> title) {
		this.title = title;
		return this;
	}

}
