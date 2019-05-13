package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.GroupingNotifier;

public class Grouping<DN extends GroupingNotifier, B extends Box> extends AbstractGrouping<DN, B> {

    public Grouping(B box) {
        super(box);
    }

}