package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.TextNotifier;

public class Text<DN extends TextNotifier, B extends Box> extends AbstractText<DN, B> {

    public Text(B box) {
        super(box);
    }

}