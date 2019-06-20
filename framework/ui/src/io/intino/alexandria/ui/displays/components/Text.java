package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.notifiers.TextNotifier;

public class Text<DN extends TextNotifier, B extends Box> extends AbstractText<DN, B> {
    private String value;

    public Text(B box) {
        super(box);
    }

    public String value() {
        return value;
    }

    public Text<DN, B> value(String value) {
        this.value = value;
        return this;
    }

    @Override
    public void update() {
        super.update();
        update(value);
    }

    public void update(String value) {
        this.value = value;
        notifier.refresh(value);
    }
}