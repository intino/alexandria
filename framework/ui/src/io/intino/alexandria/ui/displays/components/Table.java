package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.CollectionBehavior;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.notifiers.TableNotifier;
import io.intino.alexandria.ui.model.Datasource;

import java.util.stream.Collectors;

public abstract class Table<B extends Box, ItemComponent extends Row, Item> extends AbstractTable<TableNotifier, B> implements Collection<ItemComponent, Item> {

    public Table(B box) {
        super(box);
    }

	public Table source(Datasource source) {
    	source(source, new CollectionBehavior<Datasource<Item>, Item>(this));
		return this;
	}

	@Override
	protected AddItemEvent itemEvent(Display display) {
		return new AddItemEvent(this, (ItemComponent)display, ((ItemComponent)display).item());
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