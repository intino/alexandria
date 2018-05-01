package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.ui.model.Dialog;
import io.intino.konos.alexandria.ui.services.push.UISession;

public interface DialogExecution {
    Modification execute(Dialog.Toolbar.Operation operation, UISession session);

    enum Modification { None, ItemModified, CatalogModified }
}
