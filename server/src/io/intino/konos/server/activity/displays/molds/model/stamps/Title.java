package io.intino.konos.server.activity.displays.molds.model.stamps;

import io.intino.konos.server.activity.displays.molds.model.Stamp;

public class Title extends Stamp<String> {

	@Override
	public String value(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
