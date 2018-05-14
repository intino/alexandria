package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.services.push.UISession;

public class TemporalCatalogRange extends Stamp<String> {
	private String format;

	public String format() {
		return format;
	}

	public TemporalCatalogRange format(String format) {
		this.format = format;
		return this;
	}

	@Override
	public String objectValue(Object object, UISession session) {
		return null;
	}
}
