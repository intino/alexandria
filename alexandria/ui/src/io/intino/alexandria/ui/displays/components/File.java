package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.MimeTypes;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.FileInfo;
import io.intino.alexandria.ui.displays.notifiers.FileNotifier;
import io.intino.alexandria.ui.resources.Asset;

import java.net.URL;

public class File<DN extends FileNotifier, B extends Box> extends AbstractFile<DN, B> {
	private URL value;
	private String mimeType;

	public File(B box) {
		super(box);
	}

	@Override
	public void init() {
		super.init();
		refresh();
	}

	public URL value() {
		return value;
	}

	public File value(URL value) {
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

	private String serializedValue() {
		return value != null ? Asset.toResource(baseAssetUrl(), value).toUrl().toString() : null;
	}

	private String typeOf(URL value) {
		return MimeTypes.getFromFilename(value.toString());
	}

}