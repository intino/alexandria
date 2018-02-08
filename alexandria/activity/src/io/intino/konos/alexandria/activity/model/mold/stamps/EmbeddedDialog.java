package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.displays.AlexandriaDialog;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.User;

public class EmbeddedDialog extends Stamp<String> {
	private String type;
	private DialogBuilder dialogBuilder;

	@Override
	public String objectValue(Object object, User user) {
		return null;
	}

	public AlexandriaDialog createDialog(User user) {
		return dialogBuilder != null ? dialogBuilder.dialog(name(), user) : null;
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
		AlexandriaDialog dialog(String name, User user);
	}
}
