package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.operation.DownloadCollectionEvent;
import io.intino.alexandria.ui.displays.events.operation.DownloadCollectionListener;
import io.intino.alexandria.ui.displays.notifiers.DownloadCollectionNotifier;
import io.intino.alexandria.ui.spark.UIFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class DownloadCollection<DN extends DownloadCollectionNotifier, B extends Box> extends AbstractDownloadCollection<DN, B> {
	private DownloadCollectionListener downloadListener;
	private java.util.List<Collection> collections = new ArrayList<>();

    public DownloadCollection(B box) {
        super(box);
    }

	public DownloadCollection<DN, B> bindTo(Collection... collections) {
		this.collections = Arrays.stream(collections).filter(Objects::nonNull).collect(toList());
		return this;
	}

	public void onExecute(DownloadCollectionListener listener) {
		this.downloadListener = listener;
	}

	public UIFile execute() {
		if (this.downloadListener == null) return defaultFile();
		return this.downloadListener.accept(new DownloadCollectionEvent(this, collections));
	}

}