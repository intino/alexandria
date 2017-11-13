package io.intino.konos.alexandria.activity.model.toolbar;

public class Operation {
	private String name;
	private String title;
	private String alexandriaIcon;

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

	public String alexandriaIcon() {
		return alexandriaIcon;
	}

	public Operation alexandriaIcon(String icon) {
		this.alexandriaIcon = icon;
		return this;
	}
}
