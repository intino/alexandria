package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.services.push.UISession;

public class ItemLinks extends Stamp<Links> {
	private Value<String> title;

	public String title(Item item, UISession session) {
		return objectTitle(item != null ? item.object() : null, session);
	}

	public String objectTitle(Object object, UISession session) {
		return title != null ? title.value(object, session) : null;
	}

	public ItemLinks title(Value<String> title) {
		this.title = title;
		return this;
	}

	@Override
	public Links objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
