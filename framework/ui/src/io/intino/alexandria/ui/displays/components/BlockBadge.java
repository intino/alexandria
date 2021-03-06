package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BlockBadgeNotifier;

public class BlockBadge<DN extends BlockBadgeNotifier, B extends Box> extends AbstractBlockBadge<B> {
    private int value;

    public BlockBadge(B box) {
        super(box);
    }

    public int value() {
        return this.value;
    }

    public void value(int value) {
        _value(value);
        notifier.refresh(value);
    }

    protected BlockBadge<DN, B> _value(int value) {
        this.value = value;
        return this;
    }
}