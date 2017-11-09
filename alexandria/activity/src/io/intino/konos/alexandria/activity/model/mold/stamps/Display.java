package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.displays.AlexandriaStampDisplay;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

public class Display extends Stamp<String> {
	private DisplayBuilder displayBuilder;

	@Override
	public String objectValue(Object object, String username) {
		return null;
	}

	public <SD extends AlexandriaStampDisplay> SD instance() {
		return displayBuilder != null ? displayBuilder.display(name()) : null;
	}

	public Display displayBuilder(DisplayBuilder builder) {
		this.displayBuilder = builder;
		return this;
	}

	public interface DisplayBuilder {
		<SD extends AlexandriaStampDisplay> SD display(String name);
	}
}
