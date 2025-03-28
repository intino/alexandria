package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.collection.CollectionItemComponent;
import io.intino.alexandria.ui.displays.notifiers.ItemNotifier;

public class Item<DN extends ItemNotifier, Type, B extends Box> extends CollectionItemComponent<DN, Type, B> {
    private Type item;

    public Item(B box) {
        super(box);
    }

    @Override
    public Type item() {
        return this.item;
    }

    @Override
    public void update(Type item) {
        item(item);
    }

    public Item<DN, Type, B> item(Type item) {
        this.item = item;
        return this;
    }

}