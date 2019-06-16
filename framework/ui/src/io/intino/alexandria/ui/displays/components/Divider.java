package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.DividerNotifier;

public class Divider<DN extends DividerNotifier, B extends Box> extends AbstractDivider<B> {

    public Divider(B box) {
        super(box);
    }

}