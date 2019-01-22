package io.intino.alexandria.ui.model.mold.stamps.icons;

import io.intino.alexandria.ui.model.mold.stamps.Icon;
import io.intino.alexandria.ui.services.push.UISession;

import java.net.URL;

public class ResourceIcon extends Icon<URL> {

	@Override
	public URL objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
