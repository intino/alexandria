package io.intino.konos.server.activity.dialogs;

import java.util.List;

public interface DialogValuesLoader {
    List<Object> values(Dialog.Tab.Input input);
}
