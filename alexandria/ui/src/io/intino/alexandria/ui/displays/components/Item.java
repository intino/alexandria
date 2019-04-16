package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

public class Item<B extends Box, Type> extends AbstractItem<B> {
    private Type item;

    public Item(B box) {
        super(box);
    }

    public Type item() {
        return this.item;
    }

    public Item<B, Type> item(Type item) {
        this.item = item;
        return this;
    }

}