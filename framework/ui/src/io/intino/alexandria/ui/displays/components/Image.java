package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.ImageNotifier;
import io.intino.alexandria.ui.resources.Asset;

import java.net.URL;

public class Image<DN extends ImageNotifier, B extends Box> extends AbstractImage<DN, B> {
	private URL defaultValue = null;

    public Image(B box) {
        super(box);
    }

	protected Image<DN, B> _defaultValue(URL defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	@Override
	String serializedValue() {
		String result = null;
		if (value() != null) result = isFile(value()) ? Asset.toResource(baseAssetUrl(), value()).toUrl().toString() : value().toString();
		else if (defaultValue != null) result = Asset.toResource(baseAssetUrl(), defaultValue).toUrl().toString();
		return result;
	}

	private boolean isFile(URL value) {
		try {
			return value.toString().startsWith("jar:") || new java.io.File(value.toURI()).isFile();
		} catch (Throwable ignored) {
			return false;
		}
	}
}