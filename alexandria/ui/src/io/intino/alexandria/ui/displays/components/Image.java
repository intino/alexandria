package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.resources.Asset;

import java.net.URL;

public class Image<B extends Box> extends AbstractImage<B> {
	private URL value;
	private URL defaultPicture = null;

    public Image(B box) {
        super(box);
    }

	@Override
	public void init() {
		super.init();
		refresh();
	}

	public String value() {
		String result = null;
		if (defaultPicture != null) result = Asset.toResource(baseAssetUrl(), defaultPicture).toUrl().toString();
		else if (value != null) result = Asset.toResource(baseAssetUrl(), value).toUrl().toString();
		return result;
	}

	public Image value(URL value) {
    	this.value = value;
    	return this;
	}

	public Image defaultImage(URL defaultPicture) {
    	this.defaultPicture = defaultPicture;
    	return this;
	}

	public void update(URL value) {
    	this.value = value;
    	refresh();
    }

	public void refresh() {
		String value = value();
		if (value == null) return;
		notifier.refresh(value);
	}

}