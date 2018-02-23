package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class CatalogTime extends Stamp<String> {
	private String format;

	public String format() {
		return format;
	}

	public CatalogTime format(String format) {
		this.format = format;
		return this;
	}

	@Override
	public String objectValue(Object object, ActivitySession session) {
		return null;
	}
}
