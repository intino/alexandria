package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.HeaderNotifier;

public class Header<DN extends HeaderNotifier, B extends Box> extends AbstractHeader<B> {

    public Header(B box) {
        super(box);
    }

}