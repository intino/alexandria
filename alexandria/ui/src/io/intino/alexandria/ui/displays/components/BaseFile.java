package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BaseFileNotifier;

public class BaseFile<DN extends BaseFileNotifier, B extends Box> extends AbstractBaseFile<B> {

    public BaseFile(B box) {
        super(box);
    }

}