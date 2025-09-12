package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Resource;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.logger.Logger;
import io.intino.alexandria.ui.displays.components.editable.Editable;
import io.intino.alexandria.ui.displays.events.*;
import io.intino.alexandria.ui.displays.notifiers.FileEditableNotifier;
import io.intino.alexandria.ui.server.UIFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileEditable<DN extends FileEditableNotifier, B extends Box> extends AbstractFileEditable<DN, B> implements Editable<DN, B> {
	private boolean readonly;
	private java.util.List<String> allowedTypes;
	protected Listener uploadingListener = null;
	protected ChangeListener changeListener = null;
	private ReadonlyListener readonlyListener = null;
	private File preview;
	private long maxSize;

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
	public FileEditable<DN, B> focus() {
		notifier.refreshFocused(true);
		return this;
	}

	@Override
	public FileEditable<DN, B> readonly(boolean readonly) {
		_readonly(readonly);
		notifyReadonly(readonly);
		return this;
	}

	public enum Type { Image, Audio, Video, Application, Text, Xml, Html, Pdf, Excel, Zip, Jar }
	public FileEditable<DN, B> allowedTypes(java.util.List<Type> types) {
		_allowedTypes(types);
		notifyAllowedTypes(types.stream().map(Enum::name).collect(Collectors.toList()));
		return this;
	}

	public FileEditable<DN, B> allowedTypesByName(java.util.List<String> types) {
		_allowedTypesByName(types);
		notifyAllowedTypes(types);
		return this;
	}

	public long maxSize() {
		return maxSize;
	}

	public FileEditable<DN, B> maxSize(long value) {
		_maxSize(value);
		notifyMaxSize(value);
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

	@Override
	public FileEditable<DN, B> onReadonly(ReadonlyListener listener) {
		this.readonlyListener = listener;
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

	protected FileEditable<DN, B> _allowedTypes(java.util.List<Type> allowedTypes) {
		_allowedTypesByName(allowedTypes.stream().map(Enum::name).collect(Collectors.toList()));
		return this;
	}

	protected FileEditable<DN, B> _allowedTypesByName(java.util.List<String> allowedTypes) {
		this.allowedTypes = allowedTypes;
		return this;
	}

	protected FileEditable<DN, B> _maxSize(long value) {
		this.maxSize = value;
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

	private void notifyReadonly(boolean value) {
		if (readonlyListener != null) readonlyListener.accept(new ReadonlyEvent(this, value));
		notifier.refreshReadonly(value);
	}

	private void notifyAllowedTypes(java.util.List<String> types) {
		notifier.refreshAllowedTypes(new ArrayList<>(types));
	}

	private void notifyMaxSize(long value) {
		notifier.refreshMaxSize(value);
	}

}