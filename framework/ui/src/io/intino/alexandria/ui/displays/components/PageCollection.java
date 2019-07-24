package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.schemas.PageCollectionSetup;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.behaviors.PageCollectionBehavior;
import io.intino.alexandria.ui.displays.events.collection.AddItemEvent;
import io.intino.alexandria.ui.displays.notifiers.PageCollectionNotifier;
import io.intino.alexandria.ui.model.datasource.PageDatasource;

public abstract class PageCollection<DN extends PageCollectionNotifier, B extends Box> extends AbstractPageCollection<DN, B> {
    private int pageSize;

    public PageCollection(B box) {
        super(box);
    }

    public void notifyItemsRendered(io.intino.alexandria.schemas.CollectionItemsRenderedInfo info) {
        promisedChildren(info.items()).forEach(this::register);
        children(info.visible()).forEach(c -> addItemListener().ifPresent(l -> l.accept(itemEvent(c))));
        notifyRefresh();
    }

    public void loadMoreItems(CollectionMoreItems info) {
        PageCollectionBehavior behavior = behavior();
        behavior.moreItems(info);
    }

    public void changePage(Integer page) {
        PageCollectionBehavior behavior = behavior();
        behavior.page(page);
        notifier.refresh();
    }

    public void changePageSize(Integer size) {
        PageCollectionBehavior behavior = behavior();
        behavior.pageSize(size);
        notifier.refresh();
    }

    public PageCollection<DN, B> pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    protected abstract AddItemEvent itemEvent(Display c);

    @Override
    void setup() {
        PageDatasource source = source();
        if (source == null) return;
        PageCollectionBehavior behavior = behavior();
        behavior.setup(source, pageSize);
        notifier.setup(new PageCollectionSetup().pageSize(pageSize).itemCount(behavior.itemCount()));
        notifyReady();
    }

}