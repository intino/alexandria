package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.behaviors.PageCollectionBehavior;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.notifiers.ListNotifier;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.PageDatasource;

public abstract class Magazine<B extends Box, ItemComponent extends io.intino.alexandria.ui.displays.components.Item, Item> extends AbstractList<ListNotifier, B> implements Collection<ItemComponent, Item> {

    public Magazine(B box) {
        super(box);
    }

    @Override
    public <D extends Datasource> void source(D source) {
        source(source, new PageCollectionBehavior<PageDatasource<Item>, Item>(this));
    }

    @Override
    protected AddItemEvent itemEvent(Display display, int index) {
        return new AddItemEvent(this, (ItemComponent)display, ((ItemComponent)display).item(), index);
    }

}