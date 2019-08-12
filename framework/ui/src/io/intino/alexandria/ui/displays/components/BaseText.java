package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.BaseTextNotifier;

public class BaseText<DN extends BaseTextNotifier, B extends Box> extends AbstractBaseText<DN, B> {
    private String value;

    public BaseText(B box) {
        super(box);
    }

    public String value() {
        return value;
    }

    public void value(String value) {
        _value(value);
        notifier.refresh(value);
    }

    protected BaseText<DN, B> _value(String value) {
        this.value = value;
        return this;
    }

}