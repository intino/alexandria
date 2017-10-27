package io.intino.konos.server.activity.displays.dialogs;

import io.intino.konos.server.activity.displays.dialogs.model.Dialog;

public interface DialogExecution {
    Modification execute(Dialog.Toolbar.Operation operation, String username);

    enum Modification { None, ItemModified, CatalogModified }
}
