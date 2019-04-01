package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

import java.time.Instant;

public class Date<B extends Box> extends AbstractDate<B> {
    private Instant min;
    private Instant max;
    private Instant value;

    public Date(B box) {
        super(box);
    }

    public Instant value() {
        return value;
    }

    public Date<B> value(Instant value) {
        this.value = value;
        return this;
    }

    public Instant min() {
        return this.min;
    }

    public Date<B> min(Instant min) {
        this.min = min;
        return this;
    }

    public Instant max() {
        return this.max;
    }

    public Date<B> max(Instant max) {
        this.max = max;
        return this;
    }

    public void update(Instant value) {
        value(value);
        notifier.refresh(value);
    }
}