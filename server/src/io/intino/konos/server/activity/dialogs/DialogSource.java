package io.intino.konos.server.activity.dialogs;

import java.util.List;

public interface DialogSource {
    List<String> options(Dialog.Tab.Input input);
}
