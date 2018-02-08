package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.displays.AlexandriaStamp;
import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.User;

public class EmbeddedDisplay extends Stamp<String> {
	private String type;
	private DisplayBuilder displayBuilder;

	@Override
	public String objectValue(Object object, User user) {
		return null;
	}

	public AlexandriaStamp createDisplay(User user) {
		return displayBuilder != null ? displayBuilder.display(name(), user) : null;
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
		AlexandriaStamp display(String name, User user);
	}
}
