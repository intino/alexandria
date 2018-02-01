package io.intino.konos.alexandria.activity.model.mold.stamps.operations;

import io.intino.konos.alexandria.activity.displays.AlexandriaDialog;
import io.intino.konos.alexandria.activity.model.Item;
import io.intino.konos.alexandria.activity.model.mold.stamps.Operation;

public class OpenDialogOperation extends Operation<String> {
	private int width = 100;
	private DialogBuilder dialogBuilder;

	public int width() {
		return this.width;
	}

	public OpenDialogOperation width(int width) {
		this.width = width;
		return this;
	}

	public AlexandriaDialog createDialog(Item item, String username) {
		AlexandriaDialog dialog = dialogBuilder != null ? dialogBuilder.dialog(item != null ? item.object() : null, username) : null;
		if (dialog == null) return null;
		dialog.width(width);
		dialog.height(height());
		return dialog;
	}

	public OpenDialogOperation dialogBuilder(DialogBuilder dialogBuilder) {
		this.dialogBuilder = dialogBuilder;
		return this;
	}

	public interface DialogBuilder {
		AlexandriaDialog dialog(Object item, String username);
	}
}
