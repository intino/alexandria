package io.intino.konos.server.activity.displays.dialogs;

import io.intino.konos.server.activity.displays.dialogs.model.Dialog;

import java.util.List;

public interface DialogValuesManager {
    List<Object> values(Dialog.Tab.Input input);
    void values(Dialog.Tab.Input input, List<Object> values);
}
