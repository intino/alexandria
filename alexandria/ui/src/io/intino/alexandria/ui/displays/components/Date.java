package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

import java.time.Instant;

public class Date<B extends Box> extends AbstractDate<B> {
    private Instant value;

    public Date(B box) {
        super(box);
    }

    public Instant value() {
        return value;
    }

    public void update(Instant value) {
        this.value = value;
        notifier.refresh(value);
    }
}