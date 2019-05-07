package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.operation.DownloadEvent;
import io.intino.alexandria.ui.displays.events.operation.DownloadListener;
import io.intino.alexandria.ui.displays.notifiers.DownloadNotifier;

import java.util.List;

public class Download<DN extends DownloadNotifier, B extends Box> extends AbstractDownload<DN, B> {
    private String option = null;
    private java.util.List<String> options;
    private DownloadListener downloadListener;

    public Download(B box) {
        super(box);
    }

    public List<String> options() {
        return options;
    }

    public Download<DN, B> options(List<String> options) {
        this.options = options;
        return this;
    }

    public Download<DN, B> select(String option) {
        this.option = option;
        return this;
    }

    public void changeSelection(String selection) {
        this.option = selection;
    }

    public void onExecute(DownloadListener listener) {
        this.downloadListener = listener;
    }

    @Override
    public void execute() {
        if (this.downloadListener == null) return;
        notifier.refreshResult(this.downloadListener.accept(new DownloadEvent(this, option)));
    }

}