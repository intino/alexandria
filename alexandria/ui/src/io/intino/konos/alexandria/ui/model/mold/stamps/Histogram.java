package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.services.push.UISession;

public class Histogram extends Chart<HistogramData> {

	@Override
	public HistogramData objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
