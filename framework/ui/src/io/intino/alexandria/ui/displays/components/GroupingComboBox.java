package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.GroupingComboBoxNotifier;

public class GroupingComboBox<DN extends GroupingComboBoxNotifier, B extends Box> extends AbstractGroupingComboBox<DN, B> {

    public GroupingComboBox(B box) {
        super(box);
    }

}