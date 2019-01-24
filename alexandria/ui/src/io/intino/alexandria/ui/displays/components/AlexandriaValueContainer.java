package io.intino.alexandria.ui.displays.components;

import io.intino.alexandria.core.Box;
import io.intino.alexandria.ui.displays.AlexandriaComponent;
import io.intino.alexandria.ui.displays.notifiers.AlexandriaComponentNotifier;

public abstract class AlexandriaValueContainer<DN extends AlexandriaComponentNotifier, T, B extends Box> extends AlexandriaComponent<DN, B> {
    private T value;

    public AlexandriaValueContainer(B box) {
        super(box);
    }

    @Override
    protected void init() {
        super.init();
        notifyValue(value);
    }

    @Override
    public void refresh() {
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