package io.intino.konos.alexandria.activity.box.displays;

import io.intino.konos.alexandria.activity.box.model.Dialog;

public interface DialogExecution {
    Modification execute(Dialog.Toolbar.Operation operation, String username);

    enum Modification { None, ItemModified, CatalogModified }
}
