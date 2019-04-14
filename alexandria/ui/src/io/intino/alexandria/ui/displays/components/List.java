package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.PageManager;
import io.intino.alexandria.ui.model.Datasource;

public abstract class List<B extends Box, Item, ItemMold> extends AbstractList<B> {
    private Datasource source;
    private int pageSize;
    private PageManager<Item> pageManager;

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

    public void addAll(java.util.List<Item> items) {
        items.forEach(this::add);
    }

    public abstract ItemMold add(Item item);
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
        children(info.visible()).forEach(Display::refresh);
    }

}