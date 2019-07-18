package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.DialogNotifier;

public class Dialog<DN extends DialogNotifier, B extends Box> extends AbstractDialog<DN, B> {

    public Dialog(B box) {
        super(box);
    }

}