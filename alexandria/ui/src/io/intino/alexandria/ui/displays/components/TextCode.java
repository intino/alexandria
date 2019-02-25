package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;

public class TextCode<B extends Box> extends AbstractTextCode<B> {
    private String value;

    public TextCode(B box) {
        super(box);
    }

    public String value() {
        return value;
    }

    public void update(String value) {
        this.value = value;
        notifier.refresh(value);
    }

}