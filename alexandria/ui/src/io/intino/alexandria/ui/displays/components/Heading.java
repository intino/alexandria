package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.HeadingNotifier;

public class Heading<DN extends HeadingNotifier, B extends Box> extends AbstractHeading<B> {

    public Heading(B box) {
        super(box);
    }

}