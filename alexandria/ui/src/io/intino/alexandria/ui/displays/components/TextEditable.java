package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.KeyPressEventData;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.events.KeyPressEvent;
import io.intino.alexandria.ui.displays.events.KeyPressListener;

public class TextEditable<B extends Box> extends AbstractTextEditable<B> {
    private String value;
    private ChangeListener changeListener = null;
    private KeyPressListener keyPressListener = null;

    public TextEditable(B box) {
        super(box);
    }

    public String value() {
        return value;
    }

    public TextEditable<B> value(String value) {
        this.value = value;
        return this;
    }

    public void update(String value) {
        this.value = value;
        notifier.refresh(value);
    }

    public TextEditable<B> onChange(ChangeListener listener) {
        this.changeListener = listener;
        return this;
    }

    public TextEditable<B> onKeyPress(KeyPressListener listener) {
        this.keyPressListener = listener;
        return this;
    }

    public void notifyChange(String value) {
        this.value = value;
        if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
    }

    public void notifyKeyPress(KeyPressEventData data) {
        this.value = data.value();
        if (keyPressListener != null) keyPressListener.accept(new KeyPressEvent(this, data.value(), data.keyCode()));
    }
}