package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.User;

public abstract class Icon<O> extends Stamp<O> {
	private Value<String> title;

	public String title(Item item, User user) {
		return objectTitle(item != null ? item.object() : null, user);
	}

	public String objectTitle(Object object, User user) {
		return title != null ? title.value(object, user) : "";
	}

	public Icon title(Value<String> title) {
		this.title = title;
		return this;
	}

}
