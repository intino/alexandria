package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.model.Dialog;

public interface DialogExecution {
    Modification execute(Dialog.Toolbar.Operation operation, String username);

    enum Modification { None, ItemModified, CatalogModified }
}
