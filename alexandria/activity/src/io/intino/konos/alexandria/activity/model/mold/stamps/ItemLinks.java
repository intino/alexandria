package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class ItemLinks extends Stamp<Links> {
	private Value<String> title;

	public String title(Item item, ActivitySession session) {
		return objectTitle(item != null ? item.object() : null, session);
	}

	public String objectTitle(Object object, ActivitySession session) {
		return title != null ? title.value(object, session) : null;
	}

	public ItemLinks title(Value<String> title) {
		this.title = title;
		return this;
	}

	@Override
	public Links objectValue(Object object, ActivitySession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
