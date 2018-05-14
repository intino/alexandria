package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.ui.model.Dialog;

import java.util.List;

public interface DialogValuesManager {
    List<Object> values(Dialog.Tab.Input input);
    void values(Dialog.Tab.Input input, List<Object> values);
}
