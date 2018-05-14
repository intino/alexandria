package io.intino.konos.alexandria.ui.displays;

import io.intino.konos.alexandria.ui.model.Dialog;
import io.intino.konos.alexandria.ui.model.dialog.DialogResult;
import io.intino.konos.alexandria.ui.services.push.UISession;

public interface DialogExecution {
    DialogResult execute(Dialog.Toolbar.Operation operation, UISession session);
}
