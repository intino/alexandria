package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.User;

import java.net.URL;

public class Location extends Stamp<String> {
	private Value<URL> icon;
	private Value<String> drawingColor;

	public URL icon(Item item, User user) {
		return objectIcon(item != null ? item.object() : null, user);
	}

	public URL objectIcon(Object object, User user) {
		return this.icon != null ? this.icon.value(object, user) : null;
	}

	public Location icon(Value<URL> icon) {
		this.icon = icon;
		return this;
	}

	public String drawingColor(Item item, User user) {
		return objectColor(item != null ? item.object() : null, user);
	}

	public String objectColor(Object object, User user) {
		return this.drawingColor != null ? this.drawingColor.value(object, user) : null;
	}

	public Location drawingColor(Value<String> color) {
		this.drawingColor = color;
		return this;
	}

	@Override
	public String objectValue(Object object, User user) {
		return value() != null ? value().value(object, user) : null;
	}

}
