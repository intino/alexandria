package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

public class BlockBadge<B extends Box> extends AbstractBlockBadge<B> {
    private int value;

    public BlockBadge(B box) {
        super(box);
    }

    public int value() {
        return this.value;
    }

    public BlockBadge<B> value(int value) {
        this.value = value;
        return this;
    }

    public void update(int value) {
        value(value);
        notifier.refresh(value);
    }
}