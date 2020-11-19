package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.GroupingNotifier;
import io.intino.alexandria.ui.displays.notifiers.GroupingToolbarNotifier;

public class Grouping<DN extends GroupingNotifier, B extends Box> extends AbstractGrouping<DN, B> {

    public Grouping(B box) {
        super(box);
    }

    public Grouping<DN, B> pageSize(int pageSize) {
        notifier.refreshPageSize(pageSize);
        return this;
    }

}