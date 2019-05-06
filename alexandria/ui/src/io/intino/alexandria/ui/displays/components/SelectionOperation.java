package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.SelectionOperationNotifier;

public class SelectionOperation<DN extends SelectionOperationNotifier, B extends Box> extends AbstractSelectionOperation<DN, B> {

    public SelectionOperation(B box) {
        super(box);
    }

}