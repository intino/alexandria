package io.intino.konos.alexandria.activity.model.mold.stamps.icons;

import io.intino.konos.alexandria.activity.model.mold.stamps.Icon;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

import java.net.URL;

public class ResourceIcon extends Icon<URL> {

	@Override
	public URL objectValue(Object object, ActivitySession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
