package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.TextCodeNotifier;

public class TextCode<DN extends TextCodeNotifier, B extends Box> extends AbstractTextCode<DN, B> {
    private String value;

    public TextCode(B box) {
        super(box);
    }

    public String value() {
        return value;
    }

    public TextCode<DN, B> value(String value) {
        this.value = value;
        return this;
    }

    public void update(String value) {
        this.value = value;
        notifier.refresh(value);
    }

}