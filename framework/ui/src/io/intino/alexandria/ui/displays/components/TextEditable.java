package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.schemas.KeyPressEventData;
import io.intino.alexandria.ui.displays.events.*;
import io.intino.alexandria.ui.displays.notifiers.TextEditableNotifier;

public class TextEditable<DN extends TextEditableNotifier, B extends Box> extends AbstractTextEditable<DN, B> {
    private boolean readonly;
    private ChangeListener changeListener = null;
    private KeyPressListener keyPressListener = null;
    private KeyPressListener enterPressListener = null;

    private static final String EnterKeyCode = "Enter";

    public TextEditable(B box) {
        super(box);
    }

    public boolean readonly() {
        return readonly;
    }

    public TextEditable<DN, B> readonly(boolean readonly) {
        _readonly(readonly);
        notifier.refreshReadonly(readonly);
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
        _value(value);
        if (changeListener != null) changeListener.accept(new ChangeEvent(this, value));
    }

    public void notifyKeyPress(KeyPressEventData data) {
        _value(data.value());
        KeyPressEvent event = new KeyPressEvent(this, data.value(), data.keyCode());
        if (keyPressListener != null) keyPressListener.accept(event);
        if (enterPressListener != null && data.keyCode().equals(EnterKeyCode)) enterPressListener.accept(event);
    }

    protected TextEditable<DN, B> _readonly(boolean readonly) {
        this.readonly = readonly;
        return this;
    }

}