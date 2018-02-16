package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public class CardWallet extends Stamp<Wallet> {

	@Override
	public Wallet objectValue(Object object, ActivitySession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
