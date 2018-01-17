package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

import java.net.URL;

public class Location extends Stamp<String> {
	private Value<URL> icon;
	private Value<String> drawingColor;

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

	public String drawingColor(Item item, String username) {
		return objectColor(item != null ? item.object() : null, username);
	}

	public String objectColor(Object object, String username) {
		return this.drawingColor != null ? this.drawingColor.value(object, username) : null;
	}

	public Location drawingColor(Value<String> color) {
		this.drawingColor = color;
		return this;
	}

	@Override
	public String objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
