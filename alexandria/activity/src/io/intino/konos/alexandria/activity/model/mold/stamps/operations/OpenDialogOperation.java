package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.displays.AlexandriaDialog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class OpenDialogOperation extends Operation<String> {
	private int width = 100;
	private String dialogType;
	private DialogBuilder dialogBuilder;

	public OpenDialogOperation() {
		alexandriaIcon("editor:mode-edit");
	}

	public int width() {
		return this.width;
	}

	public OpenDialogOperation width(int width) {
		this.width = width;
		return this;
	}

	public String dialogType() {
		return dialogType;
	}

	public OpenDialogOperation dialogType(String dialogType) {
		this.dialogType = dialogType;
		return this;
	}

	public AlexandriaDialog createDialog(Item item, ActivitySession session) {
		AlexandriaDialog dialog = dialogBuilder != null ? dialogBuilder.dialog(item != null ? item.object() : null, session) : null;
		if (dialog == null) return null;
		dialog.width(width);
		dialog.height(height() != -1 ? height() : 100);
		return dialog;
	}

	public OpenDialogOperation dialogBuilder(DialogBuilder dialogBuilder) {
		this.dialogBuilder = dialogBuilder;
		return this;
	}

	public interface DialogBuilder {
		AlexandriaDialog dialog(Object item, ActivitySession session);
	}

}
