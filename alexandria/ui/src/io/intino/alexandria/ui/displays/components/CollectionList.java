package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.model.Datasource;

import java.util.UUID;

public abstract class CollectionList<B extends Box, Item> extends AbstractCollectionList<B> {
    private Datasource source;
    private int pageSize;

    public CollectionList(B box) {
        super(box);
    }

    @Override
    public void refresh() {
        super.refresh();
//        source.items(0, pageSize, );
    }

    public CollectionList<B, Item> pageSize(int size) {
        this.pageSize = pageSize;
        return this;
    }

    public CollectionList<B, Item> source(Datasource source) {
        this.source = source;
        return this;
    }

    public abstract Mold createMold(Item object);

    private void add(Mold mold) {
        mold.id(UUID.randomUUID().toString());
        add(mold, "items");
    }

}