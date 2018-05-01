package io.intino.konos.alexandria.ui.model.catalog.events;

import io.intino.konos.alexandria.ui.displays.AlexandriaDialog;
import io.intino.konos.alexandria.ui.model.Item;
import io.intino.konos.alexandria.ui.services.push.UISession;

public class OpenDialog extends Open {
	private int width = 100;
	private int height = 100;
	private String dialogType;
	private DialogBuilder dialogBuilder;

	public OpenDialog width(int width) {
		this.width = width;
		return this;
	}

	public OpenDialog height(int height) {
		this.height = height;
		return this;
	}

	public String dialogType() {
		return dialogType;
	}

	public OpenDialog dialogType(String dialogType) {
		this.dialogType = dialogType;
		return this;
	}

	public AlexandriaDialog createDialog(Item item, UISession session) {
		AlexandriaDialog dialog = dialogBuilder != null ? dialogBuilder.buildDialog(item != null ? item.object() : null, session) : null;
		if (dialog == null) return null;
		dialog.width(width);
		dialog.height(height);
		return dialog;
	}

	public OpenDialog dialogBuilder(DialogBuilder builder) {
		this.dialogBuilder = builder;
		return this;
	}

	public interface DialogBuilder {
		AlexandriaDialog buildDialog(Object item, UISession session);
	}
}
