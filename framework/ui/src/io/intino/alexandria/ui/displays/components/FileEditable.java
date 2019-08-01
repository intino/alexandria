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

	public FileEditable value(URL value) {
		this.value = value;
		this.mimeType = UrlUtil.mimeType(value);
		return this;
	}

	public boolean readonly() {
		return readonly;
	}

	public FileEditable<DN, B> readonly(boolean value) {
		this.readonly = readonly;
		return this;
	}

	public void update(URL value) {
		value(value);
		refresh();
	}

	public FileEditable<DN, B> updateReadonly(boolean value) {
		readonly(value);
		notifier.refreshReadonly(value);
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

	private String serializedValue() {
//		return value != null ? Asset.toResource(baseAssetUrl(), value).toUrl().toString() : null;
		return null;
	}

}