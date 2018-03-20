package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class OpenExternalDialogOperation extends Operation<String> {
	private int width = 100;
	private PathBuilder pathBuilder;
	private TitleBuilder titleBuilder;

	public OpenExternalDialogOperation() {
		alexandriaIcon("editor:mode-edit");
	}

	public int width() {
		return this.width;
	}

	public OpenExternalDialogOperation width(int width) {
		this.width = width;
		return this;
	}

	public String dialogPath(Item item, ActivitySession session) {
		return pathBuilder != null ? pathBuilder.path(item != null ? item.object() : null, session) : null;
	}

	public OpenExternalDialogOperation dialogPathBuilder(PathBuilder pathBuilder) {
		this.pathBuilder = pathBuilder;
		return this;
	}

	public String dialogTitle(Item item, ActivitySession session) {
		return titleBuilder != null ? titleBuilder.title(item != null ? item.object() : null, session) : null;
	}

	public OpenExternalDialogOperation dialogTitleBuilder(TitleBuilder titleBuilder) {
		this.titleBuilder = titleBuilder;
		return this;
	}

	public interface PathBuilder {
		String path(Object item, ActivitySession session);
	}

	public interface TitleBuilder {
		String title(Object item, ActivitySession session);
	}

}
