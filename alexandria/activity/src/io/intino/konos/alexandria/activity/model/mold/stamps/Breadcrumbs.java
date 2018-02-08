package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.User;

public class Breadcrumbs extends Stamp<Tree> {

	@Override
	public Tree objectValue(Object object, User user) {
		return value() != null ? value().value(object, user) : null;
	}

}
