package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.displays.AlexandriaDialog;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

public class EmbeddedDialog extends Stamp<String> {
	private String type;
	private DialogBuilder dialogBuilder;

	@Override
	public String objectValue(Object object, String username) {
		return null;
	}

	public AlexandriaDialog createDialog(String username) {
		return dialogBuilder != null ? dialogBuilder.dialog(name(), username) : null;
	}

	public String dialogType() {
		return type;
	}

	public EmbeddedDialog dialogType(String type) {
		this.type = type;
		return this;
	}

	public EmbeddedDialog dialogBuilder(DialogBuilder builder) {
		this.dialogBuilder = builder;
		return this;
	}

	public interface DialogBuilder {
		AlexandriaDialog dialog(String name, String username);
	}
}
