package io.intino.konos.alexandria.activity.model.mold.stamps.icons;

import io.intino.konos.alexandria.activity.model.mold.stamps.Icon;

public class AlexandriaIcon extends Icon<String> {

	@Override
	public String objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
