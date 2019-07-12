package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.ImageEditableNotifier;
import io.intino.alexandria.ui.resources.Asset;
import io.intino.alexandria.ui.utils.UrlUtil;

import java.net.URL;

public class ImageEditable<DN extends ImageEditableNotifier, B extends Box> extends AbstractImageEditable<DN, B> {
	private URL value;
	private URL defaultValue;
	private String mimeType;
	protected ChangeListener changeListener = null;

    public ImageEditable(B box) {
        super(box);
    }

	public URL value() {
		return value;
	}

	public ImageEditable value(URL value) {
		this.value = value;
		this.mimeType = UrlUtil.mimeType(value);
		return this;
	}

	public ImageEditable<DN, B> defaultValue(URL defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	public void update(URL value) {
		value(value);
		refresh();
	}

	public ImageEditable<DN, B> onChange(ChangeListener listener) {
    	this.changeListener = listener;
    	return this;
	}

	public void notifyChange(io.intino.alexandria.Resource value) {
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
	}

	@Override
	String serializedValue() {
		String result = null;
		if (value != null) result = Asset.toResource(baseAssetUrl(), value).toUrl().toString();
		else if (defaultValue != null) result = Asset.toResource(baseAssetUrl(), defaultValue).toUrl().toString();
		return result;
	}
}