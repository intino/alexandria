package io.intino.alexandria.ui.displays.components.editable;

import io.intino.alexandria.ui.displays.events.ChangeListener;

public interface Editable<DN, B> {
    boolean readonly();
    void reload();
    Editable<DN, B> readonly(boolean value);
    Editable<DN, B> onChange(ChangeListener listener);
}
