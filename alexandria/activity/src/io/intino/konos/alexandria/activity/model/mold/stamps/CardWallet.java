package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;
import io.intino.konos.alexandria.activity.services.push.User;

public class CardWallet extends Stamp<Wallet> {

	@Override
	public Wallet objectValue(Object object, User user) {
		return value() != null ? value().value(object, user) : null;
	}

}
