package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.ExportParams;
import io.intino.alexandria.ui.displays.events.operation.ExportEvent;
import io.intino.alexandria.ui.displays.events.operation.ExportListener;
import io.intino.alexandria.ui.displays.notifiers.ExportNotifier;
import io.intino.alexandria.ui.spark.UIFile;

import java.time.Instant;
import java.util.List;

public class Export<DN extends ExportNotifier, B extends Box> extends AbstractExport<DN, B> {
    private ExportParams params = new ExportParams();
    private Instant min;
    private Instant max;
    private Range range;
    private java.util.List<String> options;
    private ExportListener exportListener;

    public Export(B box) {
        super(box);
    }

    public Instant from() {
        return params.from();
    }

    public Export<DN, B> from(long from) {
        return from(Instant.ofEpochMilli(from));
    }

    public Export<DN, B> from(Instant from) {
        params.from(from);
        return this;
    }

    public Instant to() {
        return params.to();
    }

    public Export<DN, B> to(long to) {
        return to(Instant.ofEpochMilli(to));
    }

    public Export<DN, B> to(Instant to) {
        params.to(to);
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
        params.option(option);
        return this;
    }

    public void onExecute(ExportListener listener) {
        this.exportListener = listener;
    }

    public void changeParams(ExportParams selection) {
        this.params = selection;
    }

    public UIFile execute() {
        if (this.exportListener == null) return defaultFile();
        Instant from = params.from();
        Instant to = params.to();
        String option = params.option();
        return this.exportListener.accept(new ExportEvent(this, from, to, option));
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