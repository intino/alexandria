package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;

public class OpenDialogOperation extends Operation<String> {
	private int width;
	private Stamp.Value<String> path;

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

	public OpenDialogOperation path(Stamp.Value<String> path) {
		this.path = path;
		return this;
	}
}
