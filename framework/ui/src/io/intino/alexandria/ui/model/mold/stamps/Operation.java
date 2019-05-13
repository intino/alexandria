package io.intino.alexandria.ui.model.mold.stamps;

import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.model.Item;
import io.intino.alexandria.ui.services.push.UISession;

import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class Operation<O> extends Stamp<O> {
	private Mode mode = Mode.Button;
	private String alexandriaIcon = "icons:execute";

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

	public List<Object> objects(List<Item> items) {
		return items.stream().map(Item::object).collect(toList());
	}

	@Override
	public O objectValue(Object object, UISession session) {
		return null;
	}

}
