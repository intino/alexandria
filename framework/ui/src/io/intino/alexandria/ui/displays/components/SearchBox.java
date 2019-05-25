package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SearchEvent;
import io.intino.alexandria.ui.displays.events.SearchListener;
import io.intino.alexandria.ui.displays.notifiers.SearchBoxNotifier;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchBox<DN extends SearchBoxNotifier, B extends Box> extends AbstractSearchBox<B> {
    private SearchListener searchListener;
    private java.util.List<Collection> collections = new ArrayList<>();
    private String condition = null;

    public SearchBox(B box) {
        super(box);
    }

    public SearchBox<DN, B> onSearch(SearchListener listener) {
        this.searchListener = listener;
        return this;
    }

    public SearchBox<DN, B> bindTo(Collection... collections) {
        this.collections = Arrays.asList(collections);
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
        collections.forEach(c -> c.filter(condition));
        if (collections.size() > 0) notifier.refreshCount(collections.get(0).itemCount());
    }

    private void notifyListener() {
        if (searchListener == null) return;
        searchListener.accept(new SearchEvent(this, condition.trim()));
    }
}