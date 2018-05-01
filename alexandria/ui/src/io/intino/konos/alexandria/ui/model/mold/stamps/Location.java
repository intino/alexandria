package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.services.push.UISession;

import java.net.URL;

public class Location extends Stamp<String> {
	private Value<URL> icon;

	public URL icon(Item item, UISession session) {
		return objectIcon(item != null ? item.object() : null, session);
	}

	public URL objectIcon(Object object, UISession session) {
		return this.icon != null ? this.icon.value(object, session) : null;
	}

	public Location icon(Value<URL> icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public String objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
