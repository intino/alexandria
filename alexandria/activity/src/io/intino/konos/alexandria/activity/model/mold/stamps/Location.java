package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.net.URL;

public class Location extends Stamp<String> {
	private Value<URL> icon;
	private Value<String> drawingColor;

	public URL icon(Item item, ActivitySession session) {
		return objectIcon(item != null ? item.object() : null, session);
	}

	public URL objectIcon(Object object, ActivitySession session) {
		return this.icon != null ? this.icon.value(object, session) : null;
	}

	public Location icon(Value<URL> icon) {
		this.icon = icon;
		return this;
	}

	public String drawingColor(Item item, ActivitySession session) {
		return objectColor(item != null ? item.object() : null, session);
	}

	public String objectColor(Object object, ActivitySession session) {
		return this.drawingColor != null ? this.drawingColor.value(object, session) : null;
	}

	public Location drawingColor(Value<String> color) {
		this.drawingColor = color;
		return this;
	}

	@Override
	public String objectValue(Object object, ActivitySession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
