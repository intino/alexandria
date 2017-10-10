package io.intino.konos.server.activity.dialogs;

public interface DialogExecution {
    Modification execute(Dialog.Toolbar.Operation operation, String username);

    enum Modification { None, ItemModified, CatalogModified }
}
