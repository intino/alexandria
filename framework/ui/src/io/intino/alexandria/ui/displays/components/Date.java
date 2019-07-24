package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.DateNotifier;

import java.time.Instant;

public class Date<DN extends DateNotifier, B extends Box> extends AbstractDate<DN, B> {
    private Instant min;
    private Instant max;
    private Instant value;

    public Date(B box) {
        super(box);
    }

    public Instant value() {
        return value;
    }

    public Date<DN, B> value(Instant value) {
        this.value = value;
        return this;
    }

    public Instant min() {
        return this.min;
    }

    public Date<DN, B> min(Instant min) {
        this.min = min;
        return this;
    }

    public Instant max() {
        return this.max;
    }

    public Date<DN, B> max(Instant max) {
        this.max = max;
        return this;
    }

    public void update(Instant value) {
        value(value);
        notifier.refresh(value);
    }

}