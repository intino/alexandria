package io.intino.konos.alexandria.activity.model.toolbar;

import io.intino.konos.alexandria.activity.displays.AlexandriaDialog;
import io.intino.konos.alexandria.activity.services.push.User;

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

	public AlexandriaDialog createDialog(User user) {
		AlexandriaDialog dialog = dialogBuilder != null ? dialogBuilder.buildDialog(user) : null;
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
		AlexandriaDialog buildDialog(User user);
	}
}