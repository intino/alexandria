package io.intino.konos.alexandria.ui.model.views.set.item;

import io.intino.konos.alexandria.ui.model.views.set.AbstractItem;

import java.util.ArrayList;
import java.util.List;

public class Group extends AbstractItem {
    private String label;
    private Mode mode;
    private List<AbstractItem> itemList = new ArrayList<>();

    public String label() {
        return label;
    }

    public Group label(String label) {
        this.label = label;
        return this;
    }

    public Mode mode() {
        return mode;
    }

    public Group mode(Mode mode) {
        this.mode = mode;
        return this;
    }

    public List<AbstractItem> items() {
        return itemList;
    }

    public Group add(AbstractItem item) {
        this.itemList.add(item);
        item.owner(this);
        return this;
    }

    public enum Mode {
        Expanded, Collapsed
    }
}
