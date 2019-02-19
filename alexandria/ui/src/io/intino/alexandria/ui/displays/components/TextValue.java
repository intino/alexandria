package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.KeyPressEventData;
import io.intino.alexandria.ui.displays.events.ChangeEvent;
import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.events.KeyPressEvent;
import io.intino.alexandria.ui.displays.events.KeyPressListener;

public class TextValue<B extends Box> extends AbstractTextValue<B> {
    private String value;
    protected ChangeListener changeListener = null;
    protected KeyPressListener keyPressListener = null;

    public TextValue(B box) {
        super(box);
    }

    public String value() {
        return value;
    }

    public void update(String value) {
        this.value = value;
        notifier.refresh(value);
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