package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SortingNotifier;

public class Sorting<DN extends SortingNotifier, B extends Box> extends AbstractSorting<B> {

    public Sorting(B box) {
        super(box);
    }

}