package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.operation.DownloadSelectionEvent;
import io.intino.alexandria.ui.displays.events.operation.DownloadSelectionListener;
import io.intino.alexandria.ui.displays.notifiers.DownloadSelectionNotifier;

import java.util.List;

public class DownloadSelection<DN extends DownloadSelectionNotifier, B extends Box> extends AbstractDownloadSelection<DN, B> {
    private String option = null;
    private java.util.List<String> options;
    private DownloadSelectionListener executeListener;

    public DownloadSelection(B box) {
        super(box);
    }

    public void onExecute(DownloadSelectionListener listener) {
        this.executeListener = listener;
    }

    public java.util.List<String> options() {
        return options;
    }

    public DownloadSelection<DN, B> options(List<String> options) {
        this.options = options;
        return this;
    }

    public DownloadSelection<DN, B> select(String option) {
        this.option = option;
        return this;
    }

    public void changeParams(String option) {
        this.option = option;
    }

    public io.intino.alexandria.ui.spark.UIFile execute() {
        if (this.executeListener == null) return defaultFile();
        return this.executeListener.accept(new DownloadSelectionEvent(this, selection(), option));
    }

}