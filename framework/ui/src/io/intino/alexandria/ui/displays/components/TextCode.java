package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.TextCodeNotifier;

public class TextCode<DN extends TextCodeNotifier, B extends Box> extends AbstractTextCode<DN, B> {

    public TextCode(B box) {
        super(box);
    }

}