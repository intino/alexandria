package io.intino.konos.alexandria.framework.box.model.mold.stamps;

import io.intino.konos.alexandria.framework.box.model.mold.Stamp;

public class Breadcrumbs extends Stamp<Tree> {

	@Override
	public Tree value(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
