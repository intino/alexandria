package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.operation.DownloadEvent;
import io.intino.alexandria.ui.displays.events.operation.DownloadListener;
import io.intino.alexandria.ui.displays.notifiers.DownloadNotifier;
import io.intino.alexandria.ui.spark.UIFile;

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

    public void changeParams(String option) {
        this.option = option;
    }

    public void onExecute(DownloadListener listener) {
        this.downloadListener = listener;
    }

    public UIFile execute() {
        if (this.downloadListener == null) return defaultFile();
        return this.downloadListener.accept(new DownloadEvent(this, option));
    }

    protected Download<DN, B> _options(List<String> options) {
        this.options = options;
        return this;
    }

    protected Download<DN, B> _select(String option) {
        this.option = option;
        return this;
    }

}