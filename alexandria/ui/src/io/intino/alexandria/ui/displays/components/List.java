package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.collection.Collection;
import io.intino.alexandria.ui.displays.components.collection.PageManager;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.events.AddItemListener;
import io.intino.alexandria.ui.model.Datasource;

public abstract class List<B extends Box, ItemComponent extends io.intino.alexandria.ui.displays.components.Item, Item> extends AbstractList<B> implements Collection<ItemComponent, Item> {
    private Datasource source;
    private int pageSize;
    private PageManager<Item> pageManager;
    private AddItemListener addItemListener;

    public List(B box) {
        super(box);
    }

    public List source(Datasource source) {
        this.source = source;
        return this;
    }

    public List pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    @Override
    public void onAddItem(AddItemListener listener) {
        this.addItemListener = listener;
    }

    public void addAll(java.util.List<Item> items) {
        items.forEach(this::add);
    }

    public abstract ItemComponent add(Item item);
    public abstract void removeAll();

    @Override
    public void init() {
        super.init();
        this.pageManager = new PageManager<>(source, pageSize);
    }

    public void nextPage() {
        addAll(pageManager.next());
    }

    public void page(Integer value) {
        removeAll();
        addAll(pageManager.page(value));
    }

    public void itemsPerPage(Integer value) {
        removeAll();
//        addAll(pageManager.pageSize(value));
    }

    public void notifyItemsRendered(io.intino.alexandria.schemas.ItemsRenderedInfo info) {
        promisedChildren(info.items()).forEach(this::register);
        children(info.visible()).forEach(c -> addItemListener.accept(new AddItemEvent(this, (ItemComponent)c, ((ItemComponent)c).item())));
    }

}