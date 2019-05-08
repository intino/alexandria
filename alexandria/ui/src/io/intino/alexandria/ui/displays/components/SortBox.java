package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SortBoxNotifier;

public class SortBox<DN extends SortBoxNotifier, B extends Box> extends AbstractSortBox<B> {

    public SortBox(B box) {
        super(box);
    }

}