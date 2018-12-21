package io.intino.alexandria.ui.model.mold.stamps;

import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.services.push.UISession;

public class Highlight extends Stamp<String> {

	@Override
	public String objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
