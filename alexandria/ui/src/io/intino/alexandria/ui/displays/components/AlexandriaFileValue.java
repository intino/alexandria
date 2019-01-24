package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaFileValueNotifier;

import java.net.URL;

public class AlexandriaFileValue<B extends Box> extends AlexandriaValueContainer<AlexandriaFileValueNotifier, URL, B> {

	public AlexandriaFileValue(B box) {
		super(box);
	}

	@Override
	protected void notifyValue(URL value) {
		notifier.update(assetUrl(value));
	}

}