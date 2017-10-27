package io.intino.konos.server.activity.displays.molds.model.stamps;

import io.intino.konos.server.activity.displays.molds.model.Stamp;

public abstract class Operation<O> extends Stamp<O> {
	@Override
	public O value(Object object, String username) {
		return null;
	}
}
