package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.notifiers.TemplateNotifier;

public class Template<DN extends TemplateNotifier, Item, B extends Box> extends Component<DN, B> {
    private Item item;

    public Template(B box) {
        super(box);
    }

    public Template<DN, Item, B> update(Item item) {
        this.item = item;
        refresh();
        return this;
    }

    public Item item() {
        return item;
    }
}