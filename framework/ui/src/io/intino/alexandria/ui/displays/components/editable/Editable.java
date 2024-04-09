package io.intino.alexandria.ui.displays.components.editable;

import io.intino.alexandria.ui.displays.events.ChangeListener;
import io.intino.alexandria.ui.displays.events.ReadonlyListener;

public interface Editable<DN, B> {
    boolean readonly();
    void reload();
    Editable<DN, B> focus();
    Editable<DN, B> readonly(boolean value);
    Editable<DN, B> onReadonly(ReadonlyListener listener);
    Editable<DN, B> onChange(ChangeListener listener);
}
