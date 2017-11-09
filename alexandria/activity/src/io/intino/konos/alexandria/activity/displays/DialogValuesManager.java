package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.model.Dialog;

import java.util.List;

public interface DialogValuesManager {
    List<Object> values(Dialog.Tab.Input input);
    void values(Dialog.Tab.Input input, List<Object> values);
}
