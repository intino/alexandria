package io.intino.konos.alexandria.framework.box.model.mold.stamps.icons;

import io.intino.konos.alexandria.framework.box.model.mold.stamps.Icon;

import java.net.URL;

public class ResourceIcon extends Icon<URL> {

	@Override
	public URL value(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
