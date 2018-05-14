package io.intino.konos.alexandria.ui.model.toolbar;

import io.intino.konos.alexandria.ui.displays.AlexandriaDialog;
import io.intino.konos.alexandria.ui.services.push.UISession;

public class OpenDialog extends Operation {
	private int width = 100;
	private int height = 100;
	private String type;
	private DialogBuilder dialogBuilder;

	public int width() {
		return width;
	}

	public OpenDialog width(int width) {
		this.width = width;
		return this;
	}

	public int height() {
		return height;
	}

	public OpenDialog height(int height) {
		this.height = height;
		return this;
	}

	public String dialogType() {
		return type;
	}

	public OpenDialog dialogType(String type) {
		this.type = type;
		return this;
	}

	public AlexandriaDialog createDialog(UISession session) {
		AlexandriaDialog dialog = dialogBuilder != null ? dialogBuilder.buildDialog(session) : null;
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
		AlexandriaDialog buildDialog(UISession session);
	}
}