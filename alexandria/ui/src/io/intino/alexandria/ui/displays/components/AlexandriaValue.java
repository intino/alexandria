package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.ui.displays.AlexandriaComponent;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaComponentNotifier;

public abstract class AlexandriaValue<DN extends AlexandriaComponentNotifier, T> extends AlexandriaComponent<DN> {
    private T value;

    @Override
    protected void init() {
        super.init();
        notifyValue(value);
    }

    protected abstract void notifyValue(T value);

    public T value() {
        return this.value;
    }

    public void value(T value) {
        this.value = value;
    }
}