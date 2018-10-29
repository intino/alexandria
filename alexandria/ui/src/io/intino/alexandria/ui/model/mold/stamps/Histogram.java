package io.intino.alexandria.ui.model.mold.stamps;

import io.intino.alexandria.ui.services.push.UISession;

public class Histogram extends Chart<HistogramData> {

	@Override
	public HistogramData objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
