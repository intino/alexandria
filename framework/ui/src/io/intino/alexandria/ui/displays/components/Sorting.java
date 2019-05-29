package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.events.SelectEvent;
import io.intino.alexandria.ui.displays.events.SelectListener;
import io.intino.alexandria.ui.displays.notifiers.SortingNotifier;

import java.util.ArrayList;
import java.util.Arrays;

public class Sorting<DN extends SortingNotifier, B extends Box> extends AbstractSorting<B> {
    private SelectListener selectListener;
    private java.util.List<Collection> collections = new ArrayList<>();
    private boolean selected = false;

    public Sorting(B box) {
        super(box);
    }

    public Sorting<DN, B> onSelect(SelectListener listener) {
        this.selectListener = listener;
        return this;
    }

    public Sorting<DN, B> bindTo(Collection... collections) {
        this.collections = Arrays.asList(collections);
        return this;
    }

    public void toggle() {
        selected = !selected;
        notifySelected();
    }

    private void notifySelected() {
        notifyCollections();
        notifyListener();
    }

    private void notifyCollections() {
        collections.forEach(c -> {
            if (selected) c.addSorting(key());
            else c.removeSorting(key());
        });
    }

    private void notifyListener() {
        if (selectListener == null) return;
        selectListener.accept(new SelectEvent(this, key(), -1));
    }

    private String key() {
        return label() != null && !label().isEmpty() ? label() : name();
    }
}