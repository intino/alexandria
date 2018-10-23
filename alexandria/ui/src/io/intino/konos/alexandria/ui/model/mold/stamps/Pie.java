package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.services.push.UISession;

public class Pie extends Chart<PieData> {

	@Override
	public PieData objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
