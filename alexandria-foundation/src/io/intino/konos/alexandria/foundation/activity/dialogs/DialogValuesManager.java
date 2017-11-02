package io.intino.konos.alexandria.foundation.activity.dialogs;

import io.intino.konos.alexandria.foundation.activity.model.Dialog;

import java.util.List;

public interface DialogValuesManager {
    List<Object> values(Dialog.Tab.Input input);
    void values(Dialog.Tab.Input input, List<Object> values);
}
