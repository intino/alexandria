package io.intino.alexandria.ui.model.mold.stamps.icons;

import io.intino.alexandria.ui.model.mold.stamps.Icon;
import io.intino.alexandria.ui.services.push.UISession;

public class Base64Icon extends Icon<String> {

	@Override
	public String objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
