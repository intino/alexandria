package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.ui.displays.notifiers.AlexandriaImageNotifier;

import java.net.URL;

public class AlexandriaImage extends AlexandriaValue<AlexandriaImageNotifier, URL> {

	@Override
	protected void notifyValue(URL value) {
		notifier.update(assetUrl(value));
	}

}