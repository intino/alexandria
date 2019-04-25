package io.intino.alexandria.ui.model.mold.stamps;

import io.intino.alexandria.ui.services.push.UISession;

public class Timeline extends Chart<TimelineData> {

	@Override
	public TimelineData objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
