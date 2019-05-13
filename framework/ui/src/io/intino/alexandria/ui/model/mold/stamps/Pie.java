package io.intino.alexandria.ui.model.mold.stamps;

import io.intino.alexandria.ui.services.push.UISession;

public class Pie extends Chart<PieData> {

	@Override
	public PieData objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
