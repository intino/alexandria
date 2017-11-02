package io.intino.alexandria.framework.box.model.mold.stamps.icons;

import io.intino.alexandria.framework.box.model.mold.stamps.Icon;

public class Base64Icon extends Icon<String> {

	@Override
	public String value(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
