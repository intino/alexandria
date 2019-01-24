package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaImageValueNotifier;

import java.net.URL;

public class AlexandriaImageValue<B extends Box> extends AlexandriaValueContainer<AlexandriaImageValueNotifier, URL, B> {

	public AlexandriaImageValue(B box) {
		super(box);
	}

	@Override
	protected void notifyValue(URL value) {
		notifier.refreshImage(assetUrl(value));
	}

	public void update(io.intino.alexandria.Resource resource) {
		this.callback(resource);
	}

}