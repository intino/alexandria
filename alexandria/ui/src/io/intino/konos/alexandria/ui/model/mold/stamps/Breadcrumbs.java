package io.intino.konos.alexandria.ui.model.mold.stamps;

import io.intino.konos.alexandria.ui.model.mold.Stamp;
import io.intino.konos.alexandria.ui.services.push.UISession;

public class Breadcrumbs extends Stamp<Tree> {

	@Override
	public Tree objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
