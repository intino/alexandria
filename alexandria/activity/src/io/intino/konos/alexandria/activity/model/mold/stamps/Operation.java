package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public abstract class Operation<O> extends Stamp<O> {
	@Override
	public O objectValue(Object object, ActivitySession session) {
		return null;
	}
}
