package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.DateNotifier;

import java.time.Instant;

public class Date<DN extends DateNotifier, B extends Box> extends AbstractDate<DN, B> {
    private Instant min;
    private Instant max;
    private Instant value;
    private String pattern;

    public Date(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        super.didMount();
        if (pattern != null) notifier.refreshPattern(pattern);
    }

    public Instant value() {
        return value;
    }

    public Instant min() {
        return this.min;
    }

    public Instant max() {
        return this.max;
    }

    public void value(Instant value) {
        _value(value);
        notifier.refresh(value);
    }

    protected Date<DN, B> _value(Instant value) {
        this.value = value;
        return this;
    }

    protected Date<DN, B> _min(Instant min) {
        this.min = min;
        return this;
    }

    protected Date<DN, B> _max(Instant max) {
        this.max = max;
        return this;
    }

    public Date<DN, B> pattern(String pattern) {
        this.pattern = pattern;
        notifier.refreshPattern(pattern);
        return this;
    }

}