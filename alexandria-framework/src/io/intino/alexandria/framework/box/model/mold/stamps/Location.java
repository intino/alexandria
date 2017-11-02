package io.intino.alexandria.framework.box.model.mold.stamps;

import io.intino.alexandria.framework.box.model.Item;
import io.intino.alexandria.framework.box.model.mold.Stamp;

import java.net.URL;

public class Location extends Stamp<String> {
	private Value<URL> icon;

	public URL icon(Item item, String username) {
		if (item == null) return null;
		return icon(item.object(), username);
	}

	public URL icon(Object object, String username) {
		return this.icon != null ? this.icon.value(object, username) : null;
	}

	public Location icon(Value<URL> icon) {
		this.icon = icon;
		return this;
	}

	@Override
	public String value(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
