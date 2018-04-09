package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class Operation<O> extends Stamp<O> {
	private Mode mode = Mode.Button;
	private String alexandriaIcon = "icons:execute";
	private Value<String> drawingColor;

	public String alexandriaIcon() {
		return this.alexandriaIcon;
	}

	public enum Mode { Button, Link, Icon, Chip }

	public Mode mode() {
		return mode;
	}

	public Operation mode(String mode) {
		return mode(Mode.valueOf(mode));
	}

	public Operation mode(Mode mode) {
		this.mode = mode;
		return this;
	}

	public Operation alexandriaIcon(String alexandriaIcon) {
		this.alexandriaIcon = alexandriaIcon;
		return this;
	}

	public String drawingColor(Item item, ActivitySession session) {
		return objectColor(item != null ? item.object() : null, session);
	}

	public String objectColor(Object object, ActivitySession session) {
		return this.drawingColor != null ? this.drawingColor.value(object, session) : null;
	}

	public Operation drawingColor(Value<String> color) {
		this.drawingColor = color;
		return this;
	}

	public List<Object> objects(List<Item> items) {
		return items.stream().map(Item::object).collect(toList());
	}

	@Override
	public O objectValue(Object object, ActivitySession session) {
		return null;
	}

}
