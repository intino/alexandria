package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.CollectionBehavior;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.events.AddItemListener;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.TableNotifier;
import io.intino.alexandria.ui.model.Datasource;

import java.util.ArrayList;

public abstract class Table<B extends Box, ItemComponent extends Row, Item> extends AbstractTable<TableNotifier, B> implements Collection<ItemComponent, Item> {
	private CollectionBehavior<ItemComponent, Item> behavior;
	private Datasource source;
	private int pageSize;
	private java.util.List<SelectionListener> selectionListeners = new ArrayList<>();
	private AddItemListener addItemListener;

    public Table(B box) {
        super(box);
    }

	public Table source(Datasource source) {
    	source(source, new CollectionBehavior<ItemComponent, Item>(this));
		return this;
	}

	@Override
	protected AddItemEvent itemEvent(Display display) {
		return new AddItemEvent(this, (ItemComponent)display, ((ItemComponent)display).item());
	}

}