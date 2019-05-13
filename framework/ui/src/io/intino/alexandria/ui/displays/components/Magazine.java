package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.CollectionBehavior;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.notifiers.ListNotifier;
import io.intino.alexandria.ui.model.Datasource;

public abstract class Magazine<B extends Box, ItemComponent extends io.intino.alexandria.ui.displays.components.Item, Item> extends AbstractList<ListNotifier, B> implements Collection<ItemComponent, Item> {

    public Magazine(B box) {
        super(box);
    }

    public Magazine<B, ItemComponent, Item> source(Datasource source) {
        source(source, new CollectionBehavior<ItemComponent, Item>(this));
        return this;
    }

    @Override
    protected AddItemEvent itemEvent(Display display) {
        return new AddItemEvent(this, (ItemComponent)display, ((ItemComponent)display).item());
    }

}