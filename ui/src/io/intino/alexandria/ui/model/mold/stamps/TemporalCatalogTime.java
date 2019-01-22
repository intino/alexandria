package io.intino.alexandria.ui.model.mold.stamps;

import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.services.push.UISession;

public class TemporalCatalogTime extends Stamp<String> {
	private String format;

	public String format() {
		return format;
	}

	public TemporalCatalogTime format(String format) {
		this.format = format;
		return this;
	}

	@Override
	public String objectValue(Object object, UISession session) {
		return null;
	}
}
