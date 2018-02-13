package io.intino.konos.alexandria.activity.model.mold.stamps.icons;

import io.intino.konos.alexandria.activity.model.mold.stamps.Icon;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class Base64Icon extends Icon<String> {

	@Override
	public String objectValue(Object object, ActivitySession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
