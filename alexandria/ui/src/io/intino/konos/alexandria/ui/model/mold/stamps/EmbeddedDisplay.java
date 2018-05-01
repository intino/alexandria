package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.displays.AlexandriaStamp;
import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.services.push.UISession;

public class EmbeddedDisplay extends Stamp<String> {
	private String type;
	private DisplayBuilder displayBuilder;

	@Override
	public String objectValue(Object object, UISession session) {
		return null;
	}

	public AlexandriaStamp createDisplay(UISession session) {
		return displayBuilder != null ? displayBuilder.display(name(), session) : null;
	}

	public String displayType() {
		return type;
	}

	public EmbeddedDisplay displayType(String type) {
		this.type = type;
		return this;
	}

	public EmbeddedDisplay displayBuilder(DisplayBuilder builder) {
		this.displayBuilder = builder;
		return this;
	}

	public interface DisplayBuilder {
		AlexandriaStamp display(String name, UISession session);
	}
}
