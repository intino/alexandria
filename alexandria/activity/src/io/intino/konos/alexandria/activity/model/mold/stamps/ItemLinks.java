package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

public class ItemLinks extends Stamp<Links> {
	private Value<String> title;

	public String title(Item item, String username) {
		if (item == null) return null;
		return objectTitle(item.object(), username);
	}

	public String objectTitle(Object object, String username) {
		return title != null ? title.value(object, username) : null;
	}

	public ItemLinks title(Value<String> title) {
		this.title = title;
		return this;
	}

	@Override
	public Links objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
