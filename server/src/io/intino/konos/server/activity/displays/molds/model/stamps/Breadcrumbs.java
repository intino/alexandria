package io.intino.konos.server.activity.displays.molds.model.stamps;

import io.intino.konos.server.activity.displays.molds.model.Stamp;

public class Breadcrumbs extends Stamp<Tree> {

	@Override
	public Tree value(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
