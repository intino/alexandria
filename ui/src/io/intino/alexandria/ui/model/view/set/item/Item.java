package io.intino.alexandria.ui.model.view.set.item;

import io.intino.alexandria.ui.model.View;
import io.intino.alexandria.ui.model.view.set.AbstractItem;

public class Item extends AbstractItem {
    private String label;
    private String icon = null;
    private int bubble = -1;
    private View view;

    public String label() {
        return label;
    }

    public Item label(String label) {
        this.label = label;
        return this;
    }

    public String icon() {
        return icon;
    }

    public Item icon(String icon) {
        this.icon = icon;
        return this;
    }

    public int bubble() {
        return bubble;
    }

    public Item bubble(int bubble) {
        this.bubble = bubble;
        return this;
    }

    public View view() {
        return view;
    }

    public Item view(View view) {
        this.view = view;
        return this;
    }
}
