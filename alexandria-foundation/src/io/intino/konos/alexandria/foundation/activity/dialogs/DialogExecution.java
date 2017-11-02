package io.intino.konos.alexandria.foundation.activity.dialogs;

import io.intino.konos.alexandria.foundation.activity.model.Dialog;

public interface DialogExecution {
    Modification execute(Dialog.Toolbar.Operation operation, String username);

    enum Modification { None, ItemModified, CatalogModified }
}
