package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

public class ºItemLinks extends Stamp<Links> {
	private Value<String> title;

	public String title(Item item, String username) {
		return objectTitle(item != null ? item.object() : null, username);
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
