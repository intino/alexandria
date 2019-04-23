package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.schemas.CollectionSetup;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.CollectionBehavior;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.events.AddItemListener;
import io.intino.alexandria.ui.model.Datasource;

public abstract class List<B extends Box, ItemComponent extends io.intino.alexandria.ui.displays.components.Item, Item> extends AbstractList<B> implements Collection<ItemComponent, Item> {
    private CollectionBehavior<ItemComponent, Item> behavior;
    private Datasource source;
    private int pageSize;
    private AddItemListener addItemListener;

    public List(B box) {
        super(box);
    }

    public List source(Datasource source) {
        this.source = source;
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

    public void notifyItemsRendered(io.intino.alexandria.schemas.CollectionItemsRenderedInfo info) {
        promisedChildren(info.items()).forEach(this::register);
        children(info.visible()).forEach(c -> addItemListener.accept(new AddItemEvent(this, (ItemComponent)c, ((ItemComponent)c).item())));
    }

    protected List pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    private void setup() {
        if (source == null || behavior != null) return;
        this.behavior = new CollectionBehavior<>(this, source, pageSize);
        notifier.setup(new CollectionSetup().itemCount(source.itemCount()).pageSize(pageSize));
        behavior.page(0);
    }

}