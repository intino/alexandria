package io.intino.konos.alexandria.activity.model.mold.stamps.icons;

import io.intino.konos.alexandria.activity.model.mold.stamps.Icon;

import java.net.URL;

public class ResourceIcon extends Icon<URL> {

	@Override
	public URL objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
