package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.ExportParams;
import io.intino.alexandria.ui.displays.events.actionable.ExportEvent;
import io.intino.alexandria.ui.displays.events.actionable.ExportListener;
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

    public Instant to() {
        return params.to();
    }

    public List<String> options() {
        return options;
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

    protected Export<DN, B> _from(long from) {
        return _from(Instant.ofEpochMilli(from));
    }

    protected Export<DN, B> _from(Instant from) {
        params.from(from);
        return this;
    }

    protected Export<DN, B> _to(long to) {
        return _to(Instant.ofEpochMilli(to));
    }

    protected Export<DN, B> _to(Instant to) {
        params.to(to);
        return this;
    }

    protected Export<DN, B> _min(long min) {
        return _min(Instant.ofEpochMilli(min));
    }

    protected Export<DN, B> _min(Instant min) {
        this.min = min;
        return this;
    }

    protected Export<DN, B> _max(long max) {
        return _max(Instant.ofEpochMilli(max));
    }

    protected Export<DN, B> _max(Instant max) {
        this.max = max;
        return this;
    }

    protected Export<DN, B> _range(int min, int max) {
        this.range = new Range(min, max);
        return this;
    }

    protected Export<DN, B> _options(List<String> options) {
        this.options = options;
        return this;
    }

    protected Export<DN, B> _select(String option) {
        if (!this.options.contains(option)) return this;
        params.option(option);
        return this;
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