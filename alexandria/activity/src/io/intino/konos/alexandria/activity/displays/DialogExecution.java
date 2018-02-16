package io.intino.konos.alexandria.activity.displays;

import io.intino.konos.alexandria.activity.model.Dialog;
import io.intino.konos.alexandria.activity.services.push.ActivitySession;

public interface DialogExecution {
    Modification execute(Dialog.Toolbar.Operation operation, ActivitySession session);

    enum Modification { None, ItemModified, CatalogModified }
}
