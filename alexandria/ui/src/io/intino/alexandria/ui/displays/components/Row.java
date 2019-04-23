package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.Component;

public class Row<DN extends RowNotifier, Type, B extends Box> extends Item<DN, Type, B> {

    public Row(B box) {
        super(box);
    }

}