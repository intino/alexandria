package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BaseImageNotifier;

public class BaseImage<DN extends BaseImageNotifier, B extends Box> extends AbstractBaseImage<DN, B> {

    public BaseImage(B box) {
        super(box);
    }

    @Override
    public void init() {
        super.init();
        refresh();
    }

    public void refresh() {
        String value = serializedValue();
        if (value == null) return;
        notifier.refresh(value);
    }

    String serializedValue() {
        return null;
    }

}