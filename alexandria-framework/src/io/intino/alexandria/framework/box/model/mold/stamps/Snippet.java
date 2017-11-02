package io.intino.alexandria.framework.box.model.mold.stamps;

import io.intino.alexandria.framework.box.model.mold.Stamp;

public class Snippet extends Stamp<String> {

	@Override
	public String value(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
