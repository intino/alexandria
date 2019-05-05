package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.CollectionItemsRenderedInfo;
import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.schemas.CollectionSetup;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.CollectionBehavior;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.events.AddItemListener;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.model.Datasource;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Table<B extends Box, ItemComponent extends Row, Item> extends AbstractTable<B> implements Collection<ItemComponent, Item> {
	private CollectionBehavior<ItemComponent, Item> behavior;
	private Datasource source;
	private int pageSize;
	private java.util.List<SelectionListener> selectionListeners = new ArrayList<>();
	private AddItemListener addItemListener;

    public Table(B box) {
        super(box);
    }

	public Table source(Datasource source) {
		this.source = source;
		this.behavior = new CollectionBehavior<>(this);
		setup();
		return this;
	}

	@Override
	public void init() {
		super.init();
		setup();
	}

	@Override
	public void onAddItem(AddItemListener listener) {
		this.addItemListener = listener;
	}

	public void moreItems(CollectionMoreItems info) {
		behavior.moreItems(info);
	}

	public void notifyItemsRendered(CollectionItemsRenderedInfo info) {
		promisedChildren(info.items()).forEach(this::register);
		children(info.visible()).forEach(c -> addItemListener.accept(new AddItemEvent(this, (ItemComponent)c, ((ItemComponent)c).item())));
	}

	public void loadMoreItems(CollectionMoreItems info) {
		behavior.moreItems(info);
	}

	public void changePage(Integer page) {
		behavior.page(page);
		notifier.refresh();
	}

	public void changePageSize(Integer size) {
		behavior.pageSize(size);
		notifier.refresh();
	}

	public void changeSelection(String[] selection) {
		selectionListeners.forEach(l -> l.accept(new SelectionEvent(this, Arrays.asList(selection))));
	}

	protected void addSelectionListener(SelectionListener listener) {
		this.selectionListeners.add(listener);
	}

	protected Table pageSize(int pageSize) {
		this.pageSize = pageSize;
		return this;
	}

	private void setup() {
		if (source == null) return;
		notifier.setup(new CollectionSetup().itemCount(source.itemCount()).pageSize(pageSize));
		behavior.setup(source, pageSize);
	}

}