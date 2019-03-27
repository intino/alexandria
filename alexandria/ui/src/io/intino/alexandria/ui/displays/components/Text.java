package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

public class Text<B extends Box> extends AbstractText<B> {
    private String value;

    public Text(B box) {
        super(box);
    }

    public String value() {
        return value;
    }

    public Text<B> value(String value) {
        this.value = value;
        return this;
    }

    public void update(String value) {
        this.value = value;
        notifier.refresh(value);
    }
}