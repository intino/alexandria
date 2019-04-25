package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.CollectionItemsRenderedInfo;
import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.schemas.CollectionSetup;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.CollectionBehavior;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.events.AddItemListener;
import io.intino.alexandria.ui.model.Datasource;

public abstract class Table<B extends Box, ItemComponent extends Row, Item> extends AbstractTable<B> implements Collection<ItemComponent, Item> {
	private CollectionBehavior<ItemComponent, Item> behavior;
	private Datasource source;
	private int pageSize;
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