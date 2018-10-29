package io.intino.alexandria.ui.model.toolbar;

import io.intino.alexandria.ui.model.Item;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class Operation {
	private String name;
	private String title;
	private Mode mode;
	private String alexandriaIcon;

	public enum Mode { Button, Link, Icon, Chip }

	public String name() {
		return name;
	}

	public Operation name(String name) {
		this.name = name;
		return this;
	}

	public String title() {
		return title;
	}

	public Operation title(String title) {
		this.title = title;
		return this;
	}

	public Operation.Mode mode() {
		return mode;
	}

	public Operation mode(String mode) {
		return mode(Operation.Mode.valueOf(mode));
	}

	public Operation mode(Operation.Mode mode) {
		this.mode = mode;
		return this;
	}

	public String alexandriaIcon() {
		return alexandriaIcon;
	}

	public Operation alexandriaIcon(String icon) {
		this.alexandriaIcon = icon;
		return this;
	}

	protected List<Object> objects(List<Item> items) {
		return items.stream().map(Item::object).collect(toList());
	}
}
