package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaBadgeNotifier;

public class AlexandriaBadge<B extends Box> extends AlexandriaValueContainer<AlexandriaBadgeNotifier, String, B> {

	public AlexandriaBadge(B box) {
		super(box);
	}

	@Override
	protected void notifyValue(String value) {
	}

}