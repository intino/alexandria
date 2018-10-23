package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.services.push.UISession;

public class Timeline extends Chart<TimelineData> {

	@Override
	public TimelineData objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
