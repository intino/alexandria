package io.intino.konos.server.activity.displays.molds.model.stamps.icons;

import io.intino.konos.server.activity.displays.molds.model.stamps.Icon;

public class SumusIcon extends Icon<String> {

	@Override
	public String value(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
