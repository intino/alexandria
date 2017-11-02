package io.intino.alexandria.framework.box.model.toolbar;

public class Operation {
	private String name;
	private String title;
	private String sumusIcon;

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

	public String sumusIcon() {
		return sumusIcon;
	}

	public Operation sumusIcon(String sumusIcon) {
		this.sumusIcon = sumusIcon;
		return this;
	}
}
