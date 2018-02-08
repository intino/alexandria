package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.model.Dialog;
import io.intino.konos.alexandria.activity.services.push.User;

public interface DialogExecution {
    Modification execute(Dialog.Toolbar.Operation operation, User user);

    enum Modification { None, ItemModified, CatalogModified }
}
