package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BaseDateNotifier;

public class BaseDate<DN extends BaseDateNotifier, B extends Box> extends AbstractBaseDate<B> {

    public BaseDate(B box) {
        super(box);
    }

}