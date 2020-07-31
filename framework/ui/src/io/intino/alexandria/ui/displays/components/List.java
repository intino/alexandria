package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.behaviors.PageCollectionBehavior;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.notifiers.ListNotifier;
import io.intino.alexandria.ui.model.Datasource;
import io.intino.alexandria.ui.model.datasource.PageDatasource;

import java.util.stream.Collectors;

public abstract class List<B extends Box, ItemComponent extends io.intino.alexandria.ui.displays.components.Item, Item> extends AbstractList<ListNotifier, B> implements Collection<ItemComponent, Item> {

    public List(B box) {
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

    @Override
    public ItemComponent add(Item item) {
        ItemComponent component = create(item);
        addPromise(component, "rows");
        return component;
    }

    @Override
    public java.util.List<ItemComponent> add(java.util.List<Item> items) {
        java.util.List<ItemComponent> components = items.stream().map(this::create).collect(Collectors.toList());
        addPromise(components, "rows");
        return components;
    }

    @Override
    public ItemComponent insert(Item item, int index) {
        ItemComponent component = create(item);
        insertPromise(component, index, "rows");
        return component;
    }

    @Override
    public java.util.List<ItemComponent> insert(java.util.List<Item> items, int from) {
        java.util.List<ItemComponent> components = items.stream().map(this::create).collect(Collectors.toList());
        insertPromise(components, from, "rows");
        return components;
    }

}