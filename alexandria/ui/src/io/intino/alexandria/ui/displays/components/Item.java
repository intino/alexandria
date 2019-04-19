package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.notifiers.ItemNotifier;

public class Item<DN extends ItemNotifier, Type, B extends Box> extends Component<DN, B> {
    private Type item;

    public Item(B box) {
        super(box);
    }

    public Type item() {
        return this.item;
    }

    public Item item(Type item) {
        this.item = item;
        return this;
    }

}