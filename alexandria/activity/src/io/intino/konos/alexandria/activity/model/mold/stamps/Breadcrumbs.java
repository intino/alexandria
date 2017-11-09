package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;

public class Breadcrumbs extends Stamp<Tree> {

	@Override
	public Tree objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
