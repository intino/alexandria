package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.displays.AlexandriaDialog;

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

	public String type() {
		return type;
	}

	public OpenDialog dialogType(String type) {
		this.type = type;
		return this;
	}

	public AlexandriaDialog createDialog(String username) {
		AlexandriaDialog dialog = dialogBuilder != null ? dialogBuilder.buildDialog(username) : null;
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
		AlexandriaDialog buildDialog(String username);
	}
}