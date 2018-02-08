package io.intino.konos.alexandria.activity.model.mold.stamps.icons;

import io.intino.konos.alexandria.activity.model.mold.stamps.Icon;
import io.intino.konos.alexandria.activity.services.push.User;

public class Base64Icon extends Icon<String> {

	@Override
	public String objectValue(Object object, User user) {
		return value() != null ? value().value(object, user) : null;
	}

}
