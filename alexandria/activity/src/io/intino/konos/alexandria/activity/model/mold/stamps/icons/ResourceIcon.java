package io.intino.konos.alexandria.activity.model.mold.stamps.icons;

import io.intino.konos.alexandria.activity.model.mold.stamps.Icon;
import io.intino.konos.alexandria.activity.services.push.User;

import java.net.URL;

public class ResourceIcon extends Icon<URL> {

	@Override
	public URL objectValue(Object object, User user) {
		return value() != null ? value().value(object, user) : null;
	}

}
