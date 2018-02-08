package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.User;

public class ItemLinks extends Stamp<Links> {
	private Value<String> title;

	public String title(Item item, User user) {
		return objectTitle(item != null ? item.object() : null, user);
	}

	public String objectTitle(Object object, User user) {
		return title != null ? title.value(object, user) : null;
	}

	public ItemLinks title(Value<String> title) {
		this.title = title;
		return this;
	}

	@Override
	public Links objectValue(Object object, User user) {
		return value() != null ? value().value(object, user) : null;
	}

}
