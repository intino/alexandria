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

	public String value() {
		String result;
		if (defaultPicture != null) result = Asset.toResource(baseAssetUrl(), defaultPicture).toUrl().toString();
		else result = Asset.toResource(baseAssetUrl(), value).toUrl().toString();
		return result;
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
		notifier.refresh(value());
	}

	public void notifyChange(io.intino.alexandria.Resource value) {

	}

}