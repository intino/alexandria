package io.intino.alexandria.framework.box.model.mold.stamps;

import io.intino.alexandria.framework.box.displays.AlexandriaStampDisplay;
import io.intino.alexandria.framework.box.model.mold.Stamp;

public class Display extends Stamp<String> {
	private DisplayBuilder displayBuilder;

	@Override
	public String value(Object object, String username) {
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
