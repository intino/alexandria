package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.StampNotifier;

public class Stamp<DN extends StampNotifier, B extends Box> extends AbstractStamp<B> {

    public Stamp(B box) {
        super(box);
    }

}