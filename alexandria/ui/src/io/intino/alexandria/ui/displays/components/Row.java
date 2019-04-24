package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;
import io.intino.alexandria.ui.displays.notifiers.RowNotifier;

public class Row<DN extends RowNotifier, Type, B extends Box> extends Component<DN, B> {
    private Type item;

    public Row(B box) {
        super(box);
    }

    public Type item() {
        return this.item;
    }

    public Row item(Type item) {
        this.item = item;
        return this;
    }
}