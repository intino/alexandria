package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.schemas.CollectionSetup;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.CollectionBehavior;
import io.intino.alexandria.ui.displays.events.AddItemEvent;
import io.intino.alexandria.ui.displays.events.AddItemListener;
import io.intino.alexandria.ui.displays.events.SelectionEvent;
import io.intino.alexandria.ui.displays.events.SelectionListener;
import io.intino.alexandria.ui.displays.notifiers.CollectionNotifier;
import io.intino.alexandria.ui.model.Datasource;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Collection<DN extends CollectionNotifier, B extends Box> extends AbstractCollection<B> {
    private CollectionBehavior behavior;
    private Datasource source;
    private int pageSize;
    private java.util.List<SelectionListener> selectionListeners = new ArrayList<>();
    private AddItemListener addItemListener;

    public Collection(B box) {
        super(box);
    }

    public Collection<DN, B> source(Datasource source, CollectionBehavior behavior) {
        this.source = source;
        this.behavior = behavior;
        setup();
        return this;
    }

    public void onAddItem(AddItemListener listener) {
        this.addItemListener = listener;
    }

    @Override
    public void init() {
        super.init();
        setup();
    }

    public void notifyItemsRendered(io.intino.alexandria.schemas.CollectionItemsRenderedInfo info) {
        promisedChildren(info.items()).forEach(this::register);
        children(info.visible()).forEach(c -> addItemListener.accept(itemEvent(c)));
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

    protected Collection<DN, B> pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    protected abstract AddItemEvent itemEvent(Display c);

    private void setup() {
        if (source == null) return;
        notifier.setup(new CollectionSetup().itemCount(source.itemCount()).pageSize(pageSize));
        behavior.setup(source, pageSize);
    }

}