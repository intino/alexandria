package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.CollectionMoreItems;
import io.intino.alexandria.schemas.PageCollectionSetup;
import io.intino.alexandria.ui.displays.Display;
import io.intino.alexandria.ui.displays.components.collection.behaviors.PageCollectionBehavior;
import io.intino.alexandria.ui.displays.events.AddCollectionItemEvent;
import io.intino.alexandria.ui.displays.notifiers.PageCollectionNotifier;
import io.intino.alexandria.ui.model.datasource.PageDatasource;

import java.util.List;

public abstract class PageCollection<DN extends PageCollectionNotifier, B extends Box> extends AbstractPageCollection<DN, B> {
    private int pageSize = PageCollectionBehavior.DefaultPageSize;

    public PageCollection(B box) {
        super(box);
    }

    @Override
    public void didMount() {
        notifier.setupPageCollection((PageCollectionSetup) new PageCollectionSetup().pageSize(pageSize).itemCount(behavior() != null ? behavior().itemCount() : 0));
        notifyReady();
    }

    public int pageSize() {
        return pageSize;
    }

    public void notifyItemsRendered(io.intino.alexandria.schemas.CollectionItemsRenderedInfo info) {
        promisedChildren(info.items()).forEach(this::register);
        List<Display> children = children(info.visible());
        for (int i=0; i<children.size(); i++) {
            final int index = i;
            addItemListener().ifPresent(l -> l.accept(itemEvent(children.get(index), index)));
        }
        notifyRefresh();
    }

    public void loadNextPage() {
        PageCollectionBehavior behavior = behavior();
        behavior.nextPage();
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

    protected PageCollection<DN, B> _pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    protected abstract AddCollectionItemEvent itemEvent(Display c, int index);

    @Override
    void setup() {
        PageDatasource source = source();
        if (source == null) return;
        PageCollectionBehavior behavior = behavior();
        behavior.setup(source, pageSize);
        notifier.setupPageCollection((PageCollectionSetup) new PageCollectionSetup().pageSize(pageSize).itemCount(behavior.itemCount()));
        notifyReady();
    }

}