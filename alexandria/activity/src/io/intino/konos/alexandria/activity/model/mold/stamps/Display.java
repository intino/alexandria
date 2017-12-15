package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.displays.AlexandriaStamp;
import io.intino.konos.alexandria.activity.model.mold.Stamp;

public class Display extends Stamp<String> {
	private DisplayBuilder displayBuilder;

	@Override
	public String objectValue(Object object, String username) {
		return null;
	}

	public AlexandriaStamp instance(String username) {
		return displayBuilder != null ? displayBuilder.display(name(), username) : null;
	}

	public Display displayBuilder(DisplayBuilder builder) {
		this.displayBuilder = builder;
		return this;
	}

	public interface DisplayBuilder {
		AlexandriaStamp display(String name, String username);
	}
}
