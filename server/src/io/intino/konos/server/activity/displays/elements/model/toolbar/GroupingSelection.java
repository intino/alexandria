package io.intino.konos.server.activity.displays.elements.model.toolbar;

public class GroupingSelection extends Operation {
	private String sumusIcon = "content-copy";

	public String sumusIcon() {
		return sumusIcon;
	}

	public GroupingSelection sumusIcon(String sumusIcon) {
		this.sumusIcon = sumusIcon;
		return this;
	}
}
