package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.MimeTypes;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.FileInfo;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.resources.Asset;

import java.io.IOException;
import java.net.URL;

public class FileEditable<B extends Box> extends AbstractFile<B> {
	private URL value;
	private String mimeType;
	protected ChangeListener changeListener = null;

	public FileEditable(B box) {
		super(box);
	}

	public URL value() {
		return value;
	}

	public FileEditable value(URL value) {
		this.value = value;
		this.mimeType = typeOf(value);
		return this;
	}

	public void update(URL value) {
		value(value);
		refresh();
	}

	public void refresh() {
		String value = serializedValue();
		if (value == null) return;
		notifier.refresh(new FileInfo().value(value).mimeType(mimeType));
	}

	public void notifyChange(URL value) {
		value(value);
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
	}

	private String serializedValue() {
		return value != null ? Asset.toResource(baseAssetUrl(), value).toUrl().toString() : null;
	}

	private String typeOf(URL value) {
		try {
			return MimeTypes.getFromStream(value.openStream());
		} catch (IOException e) {
			return MimeTypes.DEFAULT_CONTENT_TYPE;
		}
	}
}