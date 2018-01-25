package io.intino.konos.alexandria.activity.model.mold.stamps;

import io.intino.konos.alexandria.activity.model.mold.Stamp;

public class CardWallet extends Stamp<Wallet> {

	@Override
	public Wallet objectValue(Object object, String username) {
		return value() != null ? value().value(object, username) : null;
	}

}
