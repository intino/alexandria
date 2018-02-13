package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class Breadcrumbs extends Stamp<Tree> {

	@Override
	public Tree objectValue(Object object, ActivitySession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
