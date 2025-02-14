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

public class Upload<DN extends UploadNotifier, B extends Box> extends AbstractUpload<DN, B> {
	protected Listener uploadingListener = null;
	protected UploadListener executeListener = null;
	private int filesToUploadCount = 0;
	private List<Resource> files = new ArrayList<>();

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

}