package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.ImageNotifier;
import io.intino.alexandria.ui.resources.Asset;

import java.net.URL;

public class Image<DN extends ImageNotifier, B extends Box> extends AbstractImage<DN, B> {
	private URL value;
	private URL defaultValue = null;

    public Image(B box) {
        super(box);
    }

	public URL value() {
    	return value;
	}

	public Image value(URL value) {
    	this.value = value;
    	return this;
	}

	public Image defaultValue(URL defaultValue) {
    	this.defaultValue = defaultValue;
    	return this;
	}

	public void update(URL value) {
    	this.value = value;
    	refresh();
    }

    @Override
	String serializedValue() {
		String result = null;
		if (value != null) result = Asset.toResource(baseAssetUrl(), value).toUrl().toString();
		else if (defaultValue != null) result = Asset.toResource(baseAssetUrl(), defaultValue).toUrl().toString();
		return result;
	}
}