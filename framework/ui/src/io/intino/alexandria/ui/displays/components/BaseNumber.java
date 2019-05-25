package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BaseNumberNotifier;

public class BaseNumber<DN extends BaseNumberNotifier, B extends Box> extends AbstractBaseNumber<DN, B> {

    public BaseNumber(B box) {
        super(box);
    }

}