package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.displays.AlexandriaDialog;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.services.push.UISession;

public class EmbeddedDialog extends Stamp<String> {
	private String type;
	private DialogBuilder dialogBuilder;

	@Override
	public String objectValue(Object object, UISession session) {
		return null;
	}

	public AlexandriaDialog createDialog(UISession session) {
		return dialogBuilder != null ? dialogBuilder.dialog(name(), session) : null;
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
		AlexandriaDialog dialog(String name, UISession session);
	}
}
