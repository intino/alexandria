package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;

public abstract class Operation<O> extends Stamp<O> {
	@Override
	public O objectValue(Object object, String username) {
		return null;
	}
}
