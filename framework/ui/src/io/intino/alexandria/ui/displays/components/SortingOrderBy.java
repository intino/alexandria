package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SortingOrderByNotifier;

public class SortingOrderBy<DN extends SortingOrderByNotifier, B extends Box> extends AbstractSortingOrderBy<DN, B> {

    public SortingOrderBy(B box) {
        super(box);
    }

}