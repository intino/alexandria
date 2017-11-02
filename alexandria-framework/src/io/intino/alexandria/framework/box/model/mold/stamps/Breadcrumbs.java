package io.intino.alexandria.framework.box.model.mold.stamps;

import io.intino.alexandria.framework.box.model.mold.Stamp;

public class Breadcrumbs extends Stamp<Tree> {

	@Override
	public Tree value(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
