package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BaseTextNotifier;

public class BaseText<DN extends BaseTextNotifier, B extends Box> extends AbstractBaseText<DN, B> {

    public BaseText(B box) {
        super(box);
    }

}