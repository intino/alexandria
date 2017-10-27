package io.intino.konos.server.activity.displays.dialogs;

import io.intino.konos.server.activity.displays.dialogs.model.Dialog;

import java.util.List;

public interface DialogSource {
    List<String> options(Dialog.Tab.Input input);
}
