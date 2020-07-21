package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.components.collection.CollectionItemDisplay;
import io.intino.alexandria.ui.displays.notifiers.RowNotifier;

public class Row<DN extends RowNotifier, Type, B extends Box> extends Component<DN, B> implements CollectionItemDisplay<Type> {
    private Type item;

    public Row(B box) {
        super(box);
    }

    @Override
    public Type item() {
        return this.item;
    }

    @Override
    public void update(Type item) {
        item(item);
    }

    public Row item(Type item) {
        this.item = item;
        return this;
    }
}