package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.*;
import io.intino.alexandria.ui.displays.notifiers.ImageEditableNotifier;
import io.intino.alexandria.ui.resources.Asset;
import io.intino.alexandria.ui.server.UIFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ImageEditable<DN extends ImageEditableNotifier, B extends Box> extends AbstractImageEditable<DN, B> implements Editable<DN, B> {
	private URL defaultValue;
	private boolean readonly;
	public ChangeListener changeListener = null;
	private Listener uploadingListener = null;
	private ReadonlyListener readonlyListener = null;

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
	public ImageEditable<DN, B> focus() {
		notifier.refreshFocused(true);
		return this;
	}

	@Override
	public ImageEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifyReadonly(readonly);
		return this;
	}

	public UIFile download() {
    	return new UIFile() {
			@Override
			public String label() {
				if (filename() != null) return URLEncoder.encode(filename(), StandardCharsets.UTF_8);
				String path = value().getPath();
				return path.contains("/") ? path.substring(path.lastIndexOf("/")+1) : path;
			}

			@Override
			public InputStream content() {
				try {
					if (value() != null) return value().openStream();
					else if (defaultValue != null) return defaultValue.openStream();
					return new ByteArrayInputStream(new byte[0]);
				} catch (IOException ignored) {
					return new ByteArrayInputStream(new byte[0]);
				}
			}
		};
	}

	public ImageEditable<DN, B> onUploading(Listener listener) {
		this.uploadingListener = listener;
		return this;
	}

	@Override
	public ImageEditable<DN, B> onChange(ChangeListener listener) {
    	this.changeListener = listener;
    	return this;
	}

	@Override
	public ImageEditable<DN, B> onReadonly(ReadonlyListener listener) {
    	this.readonlyListener = listener;
    	return this;
	}

	public void notifyUploading() {
		if (uploadingListener != null) uploadingListener.accept(new Event(this));
	}

	public void notifyChange(io.intino.alexandria.Resource value) {
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));

	}
	public void notifyRemove() {
		notifyChange(null);
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

	private void notifyReadonly(boolean value) {
		if (readonlyListener != null) readonlyListener.accept(new ReadonlyEvent(this, value));
		notifier.refreshReadonly(value);
	}

}