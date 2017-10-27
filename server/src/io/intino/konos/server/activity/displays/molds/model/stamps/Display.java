package io.intino.konos.server.activity.displays.molds.model.stamps;

import io.intino.konos.server.activity.displays.molds.StampDisplay;
import io.intino.konos.server.activity.displays.molds.model.Stamp;

public class Display extends Stamp<String> {
	private DisplayBuilder displayBuilder;

	@Override
	public String value(Object object, String username) {
		return null;
	}

	public <SD extends StampDisplay> SD instance() {
		return displayBuilder != null ? displayBuilder.display(name()) : null;
	}

	public Display displayBuilder(DisplayBuilder builder) {
		this.displayBuilder = builder;
		return this;
	}

	public interface DisplayBuilder {
		<SD extends StampDisplay> SD display(String name);
	}
}
