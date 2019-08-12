package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Resource;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.FileInfo;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.notifiers.FileEditableNotifier;
import io.intino.alexandria.ui.utils.UrlUtil;

import java.net.URL;

public class FileEditable<DN extends FileEditableNotifier, B extends Box> extends AbstractFileEditable<DN, B> {
	private URL value;
	private String mimeType;
	private boolean readonly;
	protected ChangeListener changeListener = null;

	public FileEditable(B box) {
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

	public FileEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifier.refreshReadonly(readonly);
		return this;
	}

	public void refresh() {
		String value = serializedValue();
		if (value == null) return;
		notifier.refresh(new FileInfo().value(value).mimeType(mimeType));
	}

	public void notifyChange(Resource value) {
//		value(value);
//		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
	}

	protected FileEditable _value(URL value) {
		this.value = value;
		this.mimeType = UrlUtil.mimeType(value);
		return this;
	}

	protected FileEditable<DN, B> _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	private String serializedValue() {
//		return value != null ? Asset.toResource(baseAssetUrl(), value).toUrl().toString() : null;
		return null;
	}

}