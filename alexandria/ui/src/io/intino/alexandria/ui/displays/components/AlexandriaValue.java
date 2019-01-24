package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaValueNotifier;

public class AlexandriaValue<B extends Box> extends AlexandriaValueContainer<AlexandriaValueNotifier, String, B> {

	public AlexandriaValue(B box) {
		super(box);
	}

	@Override
	protected void notifyValue(String value) {
		notifier.update(value);
	}

}