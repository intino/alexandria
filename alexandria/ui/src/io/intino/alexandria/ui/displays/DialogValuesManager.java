package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.model.Dialog;

import java.util.List;

public interface DialogValuesManager {
    List<Object> values(Dialog.Tab.Input input);
    void values(Dialog.Tab.Input input, List<Object> values);
}
