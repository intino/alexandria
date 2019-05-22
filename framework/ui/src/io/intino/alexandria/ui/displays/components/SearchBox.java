package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SearchEvent;
import io.intino.alexandria.ui.displays.events.SearchListener;
import io.intino.alexandria.ui.displays.notifiers.SearchBoxNotifier;

public class SearchBox<DN extends SearchBoxNotifier, B extends Box> extends AbstractSearchBox<B> {
    private SearchListener searchListener;
    private Collection collection;
    private String condition = null;

    public SearchBox(B box) {
        super(box);
    }

    public SearchBox<DN, B> onSearch(SearchListener listener) {
        this.searchListener = listener;
        return this;
    }

    public SearchBox<DN, B> bindTo(Collection collection) {
        this.collection = collection;
        return this;
    }

    public void search(String condition) {
        this.condition = condition;
        notifySelected();
    }

    private void notifySelected() {
        notifyCollection();
        notifyListener();
    }

    private void notifyCollection() {
        if (collection == null) return;
        collection.filter(condition);
        notifier.refreshCount(collection.itemCount());
    }

    private void notifyListener() {
        if (searchListener == null) return;
        searchListener.accept(new SearchEvent(this, condition.trim()));
    }
}