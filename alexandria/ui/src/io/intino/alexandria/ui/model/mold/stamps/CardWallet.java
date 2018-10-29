package io.intino.alexandria.ui.model.mold.stamps;

import io.intino.alexandria.ui.model.mold.Stamp;
import io.intino.alexandria.ui.services.push.UISession;

public class CardWallet extends Stamp<Wallet> {

	@Override
	public Wallet objectValue(Object object, UISession session) {
		return value() != null ? value().value(object, session) : null;
	}

}
