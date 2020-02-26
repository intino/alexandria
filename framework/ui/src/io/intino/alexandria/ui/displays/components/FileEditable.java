package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Resource;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.FileEditableNotifier;

import java.net.URL;

public class FileEditable<DN extends FileEditableNotifier, B extends Box> extends AbstractFileEditable<DN, B> {
	private boolean readonly;
	protected Listener uploadingListener = null;
	protected ChangeListener changeListener = null;

	public FileEditable(B box) {
		super(box);
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

	public FileEditable<DN, B> onUploading(Listener listener) {
		this.uploadingListener = listener;
		return this;
	}

	public FileEditable<DN, B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public void refresh() {
		notifier.refresh(info());
	}

	public void notifyUploading() {
		if (uploadingListener != null) uploadingListener.accept(new Event(this));
	}

	public void notifyChange(Resource value) {
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
	}

	protected FileEditable<DN, B> _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

}