package io.intino.konos.alexandria.activity.box.model.mold.stamps;

import io.intino.konos.alexandria.activity.box.model.Item;
import io.intino.konos.alexandria.activity.box.model.mold.Stamp;

import java.net.URL;

public class Location extends Stamp<String> {
	private Value<URL> icon;

	public URL icon(Item item, String username) {
		if (item == null) return null;
		return objectIcon(item.object(), username);
	}

	public URL objectIcon(Object object, String username) {
		return this.icon != null ? this.icon.value(object, username) : null;
	}

	public Location icon(Value<URL> icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public String objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
