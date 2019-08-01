package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.KeyPressEventData;
import io.intino.alexandria.ui.displays.events.*;
import io.intino.alexandria.ui.displays.notifiers.TextEditableNotifier;

public class TextEditable<DN extends TextEditableNotifier, B extends Box> extends AbstractTextEditable<DN, B> {
    private String value;
    private boolean readonly;
    private ChangeListener changeListener = null;
    private KeyPressListener keyPressListener = null;
    private KeyPressListener enterPressListener = null;

    private static final String EnterKeyCode = "Enter";

    public TextEditable(B box) {
        super(box);
    }

    public String value() {
        return value;
    }

    public TextEditable<DN, B> value(String value) {
        this.value = value;
        return this;
    }

    public boolean readonly() {
        return readonly;
    }

    public TextEditable<DN, B> readonly(boolean value) {
        this.readonly = readonly;
        return this;
    }

    public void update(String value) {
        this.value = value;
        notifier.refresh(value);
    }

    public TextEditable<DN, B> updateReadonly(boolean value) {
        readonly(value);
        notifier.refreshReadonly(value);
        return this;
    }

    public TextEditable<DN, B> onChange(ChangeListener listener) {
        this.changeListener = listener;
        return this;
    }

    public TextEditable<DN, B> onKeyPress(KeyPressListener listener) {
        this.keyPressListener = listener;
        return this;
    }

    public TextEditable<DN, B> onEnterPress(KeyPressListener listener) {
        this.enterPressListener = listener;
        return this;
    }

    public void notifyChange(String value) {
        this.value = value;
        if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
    }

    public void notifyKeyPress(KeyPressEventData data) {
        this.value = data.value();
        KeyPressEvent event = new KeyPressEvent(this, data.value(), data.keyCode());
        if (keyPressListener != null) keyPressListener.accept(event);
        if (enterPressListener != null && data.keyCode().equals(EnterKeyCode)) enterPressListener.accept(event);
    }
}