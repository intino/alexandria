package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.ExportSelection;
import io.intino.alexandria.ui.displays.events.operation.ExportEvent;
import io.intino.alexandria.ui.displays.events.operation.ExportListener;
import io.intino.alexandria.ui.displays.notifiers.ExportNotifier;

import java.time.Instant;
import java.util.List;

public class Export<DN extends ExportNotifier, B extends Box> extends AbstractExport<DN, B> {
    private ExportSelection selection = new ExportSelection();
    private Instant min;
    private Instant max;
    private Range range;
    private java.util.List<String> options;
    private ExportListener exportListener;

    public Export(B box) {
        super(box);
    }

    public Instant from() {
        return selection.from();
    }

    public Export<DN, B> from(long from) {
        return from(Instant.ofEpochMilli(from));
    }

    public Export<DN, B> from(Instant from) {
        selection.from(from);
        return this;
    }

    public Instant to() {
        return selection.to();
    }

    public Export<DN, B> to(long to) {
        return to(Instant.ofEpochMilli(to));
    }

    public Export<DN, B> to(Instant to) {
        selection.to(to);
        return this;
    }

    public Export<DN, B> min(long min) {
        return min(Instant.ofEpochMilli(min));
    }

    public Export<DN, B> min(Instant min) {
        this.min = min;
        return this;
    }

    public Export<DN, B> max(long max) {
        return max(Instant.ofEpochMilli(max));
    }

    public Export<DN, B> max(Instant max) {
        this.max = max;
        return this;
    }

    public Export<DN, B> range(int min, int max) {
        this.range = new Range(min, max);
        return this;
    }

    public List<String> options() {
        return options;
    }

    public Export<DN, B> options(List<String> options) {
        this.options = options;
        return this;
    }

    public Export<DN, B> select(String option) {
        if (!this.options.contains(option)) return this;
        selection.option(option);
        return this;
    }

    public void onExecute(ExportListener listener) {
        this.exportListener = listener;
    }

    public void changeSelection(ExportSelection selection) {
        this.selection = selection;
    }

    @Override
    public void execute() {
        if (this.exportListener == null) return;
        Instant from = selection.from();
        Instant to = selection.to();
        String option = selection.option();
        notifier.refreshResult(this.exportListener.accept(new ExportEvent(this, from, to, option)));
    }

    class Range {
        final int min;
        final int max;

        Range(int min, int max) {
            this.min = min;
            this.max = max;
        }
    }
}