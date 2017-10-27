package io.intino.konos.server.activity.displays.molds.model.stamps.operations;

import io.intino.konos.server.activity.displays.molds.model.stamps.Operation;

public class OpenDialogOperation extends Operation<String> {
	private int width;
	private Value<String> path;

	public int width() {
		return this.width;
	}

	public OpenDialogOperation width(int width) {
		this.width = width;
		return this;
	}

	public String path(String itemId, String username) {
		return path != null ? path.value(itemId, username) : "";
	}

	public OpenDialogOperation path(Value<String> path) {
		this.path = path;
		return this;
	}
}
