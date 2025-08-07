package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.Resource;
import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.Event;
import io.intino.alexandria.ui.displays.events.Listener;
import io.intino.alexandria.ui.displays.events.UploadEvent;
import io.intino.alexandria.ui.displays.events.UploadListener;
import io.intino.alexandria.ui.displays.notifiers.UploadNotifier;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Upload<DN extends UploadNotifier, B extends Box> extends AbstractUpload<DN, B> {
	protected Listener uploadingListener = null;
	protected UploadListener executeListener = null;
	private int filesToUploadCount = 0;
	private List<Resource> files = new ArrayList<>();
	private java.util.List<Type> allowedTypes;

	public Upload(B box) {
		super(box);
	}

	public Upload<DN, B> onUploading(Listener listener) {
		this.uploadingListener = listener;
		return this;
	}

	public Upload<DN, B> onExecute(UploadListener listener) {
		this.executeListener = listener;
		return this;
	}

	public enum Type { Image, Audio, Video, Application, Text, Xml, Html, Pdf, Excel }
	public Upload<DN, B> allowedTypes(java.util.List<Type> types) {
		_allowedTypes(types);
		notifyAllowedTypes(types);
		return this;
	}

	public void multipleSelection(boolean value) {
		notifier.refreshMultipleSelection(value);
	}

	public void execute() {
		filesToUploadCount = 0;
		files = new ArrayList<>();
		notifier.openDialog();
	}

	public void notifyUploading(int count) {
		filesToUploadCount = count;
		if (uploadingListener != null) uploadingListener.accept(new Event(this));
	}

	public void add(Resource file) {
		files.add(file);
		notifyUploaded();
	}

	public void notifyUploaded() {
		if (filesToUploadCount != files.size()) return;
		if (executeListener == null) return;
		executeListener.accept(new UploadEvent(this, files));
	}

	protected Upload<DN, B> _allowedTypes(java.util.List<Type> allowedTypes) {
		this.allowedTypes = allowedTypes;
		return this;
	}

	private void notifyAllowedTypes(java.util.List<Type> types) {
		notifier.refreshAllowedTypes(types.stream().map(Enum::name).collect(Collectors.toList()));
	}

}