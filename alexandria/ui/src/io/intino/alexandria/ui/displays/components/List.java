package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.components.collection.PageManager;
import io.intino.alexandria.ui.model.Datasource;

public abstract class List<B extends Box, Item> extends AbstractList<B> {
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

    public abstract void addAll(java.util.List<Item> items);
    public abstract void add(Item item);

    @Override
    public void init() {
        super.init();
        this.pageManager = new PageManager<>(source, pageSize);
    }

    public void nextPage() {
        addAll(pageManager.next());
    }

}