package io.intino.alexandria.ui.displays;

import io.intino.alexandria.ui.model.Dialog;
import io.intino.alexandria.ui.model.dialog.DialogResult;
import io.intino.alexandria.ui.services.push.UISession;

public interface DialogExecution {
    DialogResult execute(Dialog.Toolbar.Operation operation, UISession session);
}
