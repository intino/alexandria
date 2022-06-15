package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Resource;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.notifiers.FileEditableNotifier;
import io.intino.alexandria.ui.spark.UIFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class FileEditable<DN extends FileEditableNotifier, B extends Box> extends AbstractFileEditable<DN, B> implements Editable<DN, B> {
	private boolean readonly;
	protected Listener uploadingListener = null;
	protected ChangeListener changeListener = null;
	private File preview;

	public FileEditable(B box) {
		super(box);
	}

	@Override
	public void didMount() {
		super.didMount();
		refresh();
	}

	@Override
	public void init() {
		super.init();
		createPreview();
	}

	@Override
	public boolean readonly() {
		return readonly;
	}

	@Override
	public void reload() {
		notifier.refresh(info());
	}

	@Override
	public FileEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifier.refreshReadonly(readonly);
		return this;
	}

	public FileEditable<DN, B> onUploading(Listener listener) {
		this.uploadingListener = listener;
		return this;
	}

	@Override
	public FileEditable<DN, B> onChange(ChangeListener listener) {
		this.changeListener = listener;
		return this;
	}

	public void refresh() {
		refreshPreview();
		notifier.refresh(info());
	}

	public void notifyUploading() {
		if (uploadingListener != null) uploadingListener.accept(new Event(this));
	}

	public void notifyChange(Resource value) {
		if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
	}

	public UIFile downloadFile() {
		URL value = value();
		return new UIFile() {
			@Override
			public String label() {
				if (filename() != null) return URLEncoder.encode(filename(), StandardCharsets.UTF_8);
				String path = value.getPath();
				return path.contains("/") ? path.substring(path.lastIndexOf("/")+1) : path;
			}

			@Override
			public InputStream content() {
				try {
					return value.openStream();
				} catch (IOException e) {
					Logger.error(e);
					return new ByteArrayInputStream(new byte[0]);
				}
			}
		};
	}

	protected FileEditable<DN, B> _readonly(boolean readonly) {
		this.readonly = readonly;
		return this;
	}

	private void createPreview() {
		preview = new File<>(box()).id(UUID.randomUUID().toString());
		add(preview, DefaultInstanceContainer);
	}

	private void refreshPreview() {
		preview.visible(value() != null);
		preview.value(value(), mimeType());
	}

}