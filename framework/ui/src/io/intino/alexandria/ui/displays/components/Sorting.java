package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;
import io.intino.alexandria.ui.displays.notifiers.SortingNotifier;

public class Sorting<DN extends SortingNotifier, B extends Box> extends AbstractSorting<B> {
    private SelectListener selectListener;
    private Collection collection;
    private boolean selected = false;

    public Sorting(B box) {
        super(box);
    }

    public Sorting<DN, B> onSelect(SelectListener listener) {
        this.selectListener = listener;
        return this;
    }

    public Sorting<DN, B> bindTo(Collection collection) {
        this.collection = collection;
        return this;
    }

    public void toggle() {
        selected = !selected;
        notifySelected();
    }

    private void notifySelected() {
        notifyCollection();
        notifyListener();
    }

    private void notifyCollection() {
        if (collection == null) return;
        if (selected) collection.addSorting(key());
        else collection.removeSorting(key());
    }

    private void notifyListener() {
        if (selectListener == null) return;
        selectListener.accept(new SelectEvent(this, key(), -1));
    }

    private String key() {
        return label() != null && !label().isEmpty() ? label() : name();
    }
}