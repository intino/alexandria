package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.MimeTypes;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.ImageEditableNotifier;
import io.intino.alexandria.ui.resources.Asset;

import java.net.URL;

public class ImageEditable<DN extends ImageEditableNotifier, B extends Box> extends AbstractImageEditable<DN, B> {
	private URL value;
	private URL defaultValue;
	private String mimeType;
	private boolean readonly;
	public ChangeListener changeListener = null;

    public ImageEditable(B box) {
        super(box);
    }

	public URL value() {
		return value;
	}

	public boolean readonly() {
		return readonly;
	}

	public void value(URL value) {
		_value(value);
		refresh();
	}

	public ImageEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifier.refreshReadonly(readonly);
		return this;
	}

	public ImageEditable<DN, B> onChange(ChangeListener listener) {
    	this.changeListener = listener;
    	return this;
	}

	public void notifyChange(io.intino.alexandria.Resource value) {
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
	}

	protected ImageEditable _value(URL value) {
		this.value = value;
		this.mimeType = MimeTypes.contentTypeOf(value);
		return this;
	}

	protected ImageEditable<DN, B> _defaultValue(URL defaultValue) {
		this.defaultValue = defaultValue;
		return this;
	}

	protected ImageEditable<DN, B> _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	@Override
	String serializedValue() {
		String result = null;
		if (value != null) result = Asset.toResource(baseAssetUrl(), value).toUrl().toString();
		else if (defaultValue != null) result = Asset.toResource(baseAssetUrl(), defaultValue).toUrl().toString();
		return result;
	}
}