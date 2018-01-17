package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

import java.net.URL;

public class Location extends Stamp<String> {
	private Value<URL> icon;
	private Value<String> color;

	public URL icon(Item item, String username) {
		return objectIcon(item != null ? item.object() : null, username);
	}

	public URL objectIcon(Object object, String username) {
		return this.icon != null ? this.icon.value(object, username) : null;
	}

	public Location icon(Value<URL> icon) {
		this.icon = icon;
		return this;
	}

	public String color(Item item, String username) {
		return objectColor(item != null ? item.object() : null, username);
	}

	public String objectColor(Object object, String username) {
		return this.color != null ? this.color.value(object, username) : null;
	}

	public Location color(Value<String> color) {
		this.color = color;
		return this;
	}

	@Override
	public String objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
