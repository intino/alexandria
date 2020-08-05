package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.MimeTypes;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.File;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.ImageEditableNotifier;
import io.intino.alexandria.ui.resources.Asset;

import java.net.URL;

public class ImageEditable<DN extends ImageEditableNotifier, B extends Box> extends AbstractImageEditable<DN, B> implements Editable<DN, B> {
	private URL defaultValue;
	private boolean readonly;
	public ChangeListener changeListener = null;

    public ImageEditable(B box) {
        super(box);
    }

	@Override
	public boolean readonly() {
		return readonly;
	}

	@Override
	public void reload() {
		notifier.refresh(serializedValue());
	}

	@Override
	public ImageEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifier.refreshReadonly(readonly);
		return this;
	}

	@Override
	public ImageEditable<DN, B> onChange(ChangeListener listener) {
    	this.changeListener = listener;
    	return this;
	}

	public void notifyChange(io.intino.alexandria.Resource value) {
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
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
		if (value() != null) result = Asset.toResource(baseAssetUrl(), value()).toUrl().toString();
		else if (defaultValue != null) result = Asset.toResource(baseAssetUrl(), defaultValue).toUrl().toString();
		return result;
	}
}